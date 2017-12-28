package wind.zhihunews.net

import wind.zhihunews.BuildConfig
import wind.zhihunews.model.News
import wind.zhihunews.model.StartImage
import wind.zhihunews.model.StoryDetail
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface Api {

    @GET("news/latest")
    fun newsLatest(): Single<News>

    @GET("news/before/{date}")
    fun newsBefore(@Path("date") date: String): Single<News>

    @GET("news/{id}")
    fun storyDetail(@Path("id") id: Int?): Single<StoryDetail>

    @GET("start-image/{width}*{height}")
    fun startImage(@Path("width") width: Int?, @Path("height") height: Int?): Single<StartImage>

    companion object {

        val BASE_URL = "http://news-at.zhihu.com/api/4/"
    }

}

val api by lazy {
    Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.HEADERS
                    })
                    .build())
            .build()
            .create(Api::class.java)!!
}
