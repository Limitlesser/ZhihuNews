package wind.zhihunews.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedCollapsingToolbarLayout
import org.jetbrains.anko.support.v4.nestedScrollView
import wind.zhihunews.R
import wind.zhihunews.base.BaseActivity
import wind.zhihunews.model.StoryDetail
import wind.zhihunews.net.api
import wind.zhihunews.service.NewsService
import wind.zhihunews.util.observe
import wind.zhihunews.widget.attrDimen
import wind.zhihunews.widget.simpleDraweeView
import wind.zhihunews.widget.transitionNameCompat


class DetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = DetailViewModel(NewsService(api))
        DetailUI(viewModel).setContentView(this)
        viewModel.storyDetail(intent.getIntExtra("id", 0))
    }
}

class DetailViewModel(val newsService: NewsService) {
    val storyDetail = MutableLiveData<StoryDetail>()

    fun storyDetail(id: Int) {
        newsService.storyDetail(id)
                .subscribe { detail -> storyDetail.postValue(detail) }
    }
}

class DetailUI(val viewModel: DetailViewModel) : AnkoComponent<DetailActivity> {

    @SuppressLint("SetJavaScriptEnabled")
    override fun createView(ui: AnkoContext<DetailActivity>): View {
        return ui.apply {
            coordinatorLayout {
                appBarLayout {
                    id = R.id.app_bar
                    themedCollapsingToolbarLayout(R.style.AppTheme_CollapsingToolbar) {
                        viewModel.storyDetail.observe(owner) { title = it.title }
                        simpleDraweeView {
                            transitionNameCompat = "shared_img"
                            viewModel.storyDetail.observe(owner) { setImageURI(it.image) }
                            layoutParams = CollapsingToolbarLayout.LayoutParams(matchParent, matchParent).apply {
                                collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                                parallaxMultiplier = 0.7f
                            }
                        }
                        val toolbar = themedToolbar(R.style.AppTheme_Toolbar_Navigation) {
                            backgroundColor = Color.TRANSPARENT
                            layoutParams = CollapsingToolbarLayout.LayoutParams(matchParent, attrDimen(R.attr.actionBarSize)).apply {
                                collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                            }
                        }
                        owner.setSupportActionBar(toolbar)
                        owner.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    }.lparams(matchParent, matchParent) {
                        scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                    }
                }.lparams(matchParent, dip(210))
                val progress = MutableLiveData<Int>()
                horizontalProgressBar {
                    progress.observe(owner) {
                        visibility = if (it in 1 until 100) View.VISIBLE else GONE
                        this.progress = it
                    }
                }.lparams(matchParent, wrapContent) {
                    anchorId = R.id.app_bar
                    anchorGravity = Gravity.BOTTOM
                }

                nestedScrollView {
                    webView {
                        settings.apply {
                            javaScriptEnabled = true
                            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                            setAppCacheEnabled(true)
                            domStorageEnabled = true
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView, newProgress: Int) {
                                progress.postValue(newProgress)
                            }
                        }
                        viewModel.storyDetail.observe(owner) {
                            loadDataWithBaseURL("x-data://base",
                                    viewModel.newsService.detail2Html(it.body),
                                    "text/html", "UTF-8", null)
                        }
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, matchParent) {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }
            }
        }.view
    }

}