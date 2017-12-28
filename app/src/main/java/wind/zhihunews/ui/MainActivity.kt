package wind.zhihunews.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.ToxicBakery.viewpager.transforms.AccordionTransformer
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
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


}

class MainViewModel(private val newsService: NewsService) : ViewModel() {
    private var date: String? = null
    val stories = MutableLiveData<List<Story>>()
    val topStories = MutableLiveData<List<TopStory>>()
    @Volatile
    var newData = true
    val isRefreshing = MutableLiveData<Boolean>()
    val isLoadingMore = MutableLiveData<Boolean>()
    val hasMoreData = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    fun news() {
        newsService.newsLatest()
                .doOnSubscribe { isRefreshing.postValue(true) }
                .doFinally { isRefreshing.postValue(false) }
                .subscribe({ news ->
                    newData = true
                    date = news.date
                    this.stories.postValue(news.stories)
                    this.topStories.postValue(news.top_stories)
                    hasMoreData.postValue(true)
                }, error::postValue)
    }

    fun newBefore() {
        if (date == null) {
            hasMoreData.postValue(false)
            return
        }
        newsService.newsBefore(date!!)
                .doOnSubscribe { isLoadingMore.postValue(true) }
                .doFinally { isLoadingMore.postValue(false) }
                .subscribe({ news ->
                    newData = false
                    date = news.date
                    this.stories.postValue(news.stories)
                    hasMoreData.postValue(news.stories.isNotEmpty())
                }, error::postValue)
    }
}

class MainUI(private val viewModel: MainViewModel) : AnkoComponent<MainActivity> {


    override fun createView(ui: AnkoContext<MainActivity>): View {
        fun toDetail(id: Int) {
            ui.startActivity<DetailActivity>("id" to id)
        }
        return ui.apply {
            verticalLayout {
                val toolbar = themedToolbar(R.style.AppTheme_Toolbar)
                        .lparams(matchParent, attrDimen(R.attr.actionBarSize))
                owner.setSupportActionBar(toolbar)
                swipeRefreshLayout {
                    viewModel.isRefreshing.observe(owner) { isRefreshing = it }
                    onRefresh { viewModel.news() }
                    recyclerView {
                        isVerticalScrollBarEnabled = true
                        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(DividerItemDecoration(ctx, LinearLayoutManager.VERTICAL))
                        endlessAdapter(
                                headAdapter(false) {
                                    adapter = baseAdapter<Story>(false) {
                                        viewModel.stories.observe(owner) {
                                            if (viewModel.newData) setData(it) else add(it)
                                        }
                                        item { source ->
                                            relativeLayout {
                                                backgroundResource = attrRes(R.attr.selectableItemBackground)
                                                val image = simpleDraweeView {
                                                    id = R.id.image
                                                    padding = dip(8)
                                                    transitionNameCompat = "shared_image"
                                                    source.subscribe { setImageURI(it.images[0]) }
                                                }.lparams(dip(100), matchParent)
                                                textView {
                                                    source.subscribe { text = it.title }
                                                }.lparams(matchParent, matchParent) {
                                                    margin = dip(8)
                                                    rightOf(image)
                                                }
                                            }.lparams(matchParent, dip(100))
                                        }
                                        itemClick { toDetail(it.id) }
                                    }
                                    addHeaderView(owner.UI(false) {
                                        convenientBanner<TopStory> {
                                            setPageTransformer(AccordionTransformer())
                                            setOnItemClickListener { }
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
                                },
                                onLoadMore = { viewModel.newBefore() },
                                loadView = R.layout.item_load_more
                        ) {
                            viewModel.isLoadingMore.observe(owner) {
                                if (!it) onDataReady(viewModel.hasMoreData.value!!)
                            }
                            viewModel.hasMoreData.observe(owner) {
                                if (it) restartAppending() else stopAppending()
                            }
                        }

                    }
                }.lparams(matchParent, matchParent)
            }
        }.view
    }
}
