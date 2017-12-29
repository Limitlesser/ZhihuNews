package wind.zhihunews.service

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.*
import wind.zhihunews.dao.database
import wind.zhihunews.model.*
import wind.zhihunews.net.Api


/**
 * Created by wind on 2016/8/17.
 */
class NewsService(private val api: Api) {

    fun newsLatest(): Single<News> {
        return api.newsLatest()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::saveNews)
                .onErrorResumeNext { Single.fromCallable { dbNews() } }
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
                .mergeWith {
                    Single.fromCallable {
                        database.use {
                            select("StoryDetail").
                                    whereArgs("id=$id").parseSingle(classParser<StoryDetail>())
                        }
                    }
                }
                .firstElement()
                .toSingle()
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
        database.use {
            delete("Story")
            delete("TopStory")
            news.stories.forEach {
                insert("Story", "id" to it.id, "title" to it.title,
                        "image" to it.firstImage)
            }
            news.top_stories?.forEach {
                insert("TopStory", "id" to it.id,
                        "type" to it.type,
                        "title" to it.title,
                        "image" to it.image,
                        "ga_prefix" to it.ga_prefix)
            }
        }
    }

    private fun saveStory(storyDetail: StoryDetail) {
        database.use {
            replace("StoryDetail", "id" to storyDetail.id,
                    "body" to storyDetail.body,
                    "title" to storyDetail.title,
                    "image" to storyDetail.image,
                    "image_source" to storyDetail.image_source,
                    "share_url" to storyDetail.share_url)
        }
    }
}
