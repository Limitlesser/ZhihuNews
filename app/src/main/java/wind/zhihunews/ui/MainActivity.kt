package wind.zhihunews.ui

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ToxicBakery.viewpager.transforms.AccordionTransformer
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import wind.zhihunews.R
import wind.zhihunews.base.BaseActivity
import wind.zhihunews.base.ViewModel
import wind.zhihunews.model.Story
import wind.zhihunews.model.TopStory
import wind.zhihunews.net.api
import wind.zhihunews.service.NewsService
import wind.zhihunews.util.drawable
import wind.zhihunews.util.observe
import wind.zhihunews.widget.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel(NewsService(api))
        MainUI(viewModel).setContentView(this)
        viewModel.news()
        viewModel.error.observe { toast(it.localizedMessage) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about) {
            startActivity<AboutActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

}

class MainViewModel(private val newsService: NewsService) : ViewModel() {
    private var date: String? = null
    val stories = MutableLiveData<List<Story>>()
    val topStories = MutableLiveData<List<TopStory>>()
    @Volatile
    var isNewData = true
    val isRefreshing = MutableLiveData<Boolean>()
    val isLoadingMore = MutableLiveData<Boolean>()
    val hasMoreData = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    fun news() {
        newsService.newsLatest()
                .doOnSubscribe { isRefreshing.postValue(true) }
                .doFinally { isRefreshing.postValue(false) }
                .subscribe({ news ->
                    isNewData = true
                    date = news.date
                    this.stories.postValue(news.stories)
                    this.topStories.postValue(news.top_stories)
                    hasMoreData.postValue(true)
                }, error::postValue)
    }

    fun newsBefore() {
        if (date == null) {
            hasMoreData.postValue(false)
            return
        }
        newsService.newsBefore(date!!)
                .doOnSubscribe { isLoadingMore.postValue(true) }
                .doFinally { isLoadingMore.postValue(false) }
                .subscribe({ news ->
                    isNewData = false
                    date = news.date
                    this.stories.postValue(news.stories)
                    hasMoreData.postValue(news.stories.isNotEmpty())
                }, error::postValue)
    }
}

class MainUI(private val viewModel: MainViewModel) : AnkoComponent<MainActivity> {


    override fun createView(ui: AnkoContext<MainActivity>): View {

        fun toDetail(view: View, id: Int) {
            ui.owner.apply {
                startActivity(intentFor<DetailActivity>("id" to id),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this, view,
                                getString(R.string.shared_img)).toBundle())
            }
        }
        return ui.owner.run {
            verticalLayout {
                toolbar(baseToolBar)
                        .lparams(matchParent, attrDimen(R.attr.actionBarSize))
                        .apply { setSupportActionBar(this) }
                swipeRefreshLayout {
                    viewModel.isRefreshing.observe { isRefreshing = it }
                    onRefresh { viewModel.news() }
                    recyclerView {
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(DividerItemDecoration(ctx, LinearLayoutManager.VERTICAL))
                        endlessAdapter(
                                headAdapter(false) {
                                    addHeaderView(UI(false) {
                                        convenientBanner<TopStory> {
                                            setPageTransformer(AccordionTransformer())
                                            setOnItemClickListener { toDetail(this, viewModel.topStories.value!![it].id) }
                                            ui.owner.lifecycle.addObserver(GenericLifecycleObserver { _, event ->
                                                @Suppress("NON_EXHAUSTIVE_WHEN")
                                                when (event) {
                                                    Lifecycle.Event.ON_RESUME -> startTurning(4000)
                                                    Lifecycle.Event.ON_PAUSE -> stopTurning()
                                                }
                                            })
                                            viewModel.topStories.observe(ui.owner) {
                                                setPages({
                                                    convenientHolder<TopStory> { data ->
                                                        frameLayout {
                                                            foreground = drawable(attrRes(R.attr.selectableItemBackground))
                                                            simpleDraweeView {
                                                                data.subscribe { imageUriString = it.image }
                                                                transitionNameCompat = owner.getString(R.string.shared_img)
                                                            }.lparams(matchParent, matchParent)
                                                            frameLayout {
                                                                backgroundColor = 0x66000000
                                                                minimumHeight = dip(52)
                                                                textView {
                                                                    gravity = Gravity.CENTER_VERTICAL
                                                                    horizontalPadding = dip(10)
                                                                    textColor = Color.WHITE
                                                                    textSize = 17f
                                                                    data.subscribe { text = it.title }
                                                                }.lparams(wrapContent, wrapContent) {
                                                                    gravity = Gravity.CENTER_VERTICAL
                                                                }
                                                            }.lparams(matchParent, wrapContent) {
                                                                gravity = Gravity.BOTTOM
                                                            }
                                                        }
                                                    }
                                                }, it)
                                            }
                                        }.lparams(matchParent, dimen(R.dimen.app_bar_large))
                                    }.view)
                                    adapter = baseAdapter<Story>(false) {
                                        viewModel.stories.observe {
                                            if (viewModel.isNewData) setData(it) else add(it)
                                        }
                                        item { source ->
                                            relativeLayout {
                                                backgroundResource = attrRes(R.attr.selectableItemBackground)
                                                simpleDraweeView {
                                                    id = R.id.image
                                                    padding = dip(8)
                                                    transitionNameCompat = owner.getString(R.string.shared_img)
                                                    source.subscribe { setImageURI(it.firstImage) }
                                                }.lparams(dip(100), matchParent)
                                                textView {
                                                    source.subscribe { text = it.title }
                                                }.lparams(matchParent, matchParent) {
                                                    margin = dip(8)
                                                    rightOf(R.id.image)
                                                }
                                            }.lparams(matchParent, dip(100))
                                        }
                                        itemClick { _, position -> toDetail(itemView.find(R.id.image), getItem(position - 1).id) }
                                    }
                                },
                                onLoadMore = { viewModel.newsBefore() },
                                loadView = R.layout.item_load_more
                        ) {
                            viewModel.isLoadingMore.observe {
                                if (!it) onDataReady(viewModel.hasMoreData.value!!)
                            }
                            viewModel.hasMoreData.observe {
                                if (it) restartAppending() else stopAppending()
                            }
                        }

                    }
                }.lparams(matchParent, matchParent)
            }
        }
    }
}
