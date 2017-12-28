package wind.zhihunews.service

import wind.zhihunews.dao.database
import wind.zhihunews.dao.replace
import wind.zhihunews.model.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import wind.zhihunews.net.Api


/**
 * Created by wind on 2016/8/17.
 */
class NewsService(private val api: Api) {

    fun newsLatest(): Single<News> {
        return api.newsLatest()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::saveNews)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun newsBefore(date: String): Single<News> {
        return api.newsBefore(date)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::saveNews)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun storyDetail(id: Int?): Single<StoryDetail> {
        return api.storyDetail(id)
                .doOnSuccess(this::saveStory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun startImage(width: Int?, height: Int?): Single<StartImage> {
        return api.startImage(width, height)
                .onErrorReturnItem(StartImage("Zhihu", "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun detail2Html(body: String): String {
        val css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">"
        var html = "<html><head>$css</head><body>$body</body></html>"
        html = html.replace("<div class=\"img-place-holder\">", "")
        return html
    }

    private fun dbNews(): News {
        return database.use {
            News(stories = select("Story").parseList(classParser<Story>()),
                    top_stories = select("TopStory").parseList(classParser<TopStory>()))
        }
    }

    private fun saveNews(news: News) {
//        database.use {
//            delete("Story")
//            delete("TopStory")
//            insertList(news.stories)
//            insertList(news.top_stories)
//        }
    }

    private fun saveStory(storyDetail: StoryDetail) {
        database.use { replace(storyDetail) }
    }
}
