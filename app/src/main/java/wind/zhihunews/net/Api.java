package wind.zhihunews.net;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wind.zhihunews.bean.News;
import wind.zhihunews.bean.StartImage;
import wind.zhihunews.db.model.StoryDetail;

/**
 * Created by wind on 2016/8/16.
 */
public interface Api {

    String BASE_URL = "http://news-at.zhihu.com/api/4/";

    @GET("news/latest")
    Observable<News> newsLatest();

    @GET("news/before/{date}")
    Observable<News> newsBefore(@Path("date") String date);

    @GET("news/{id}")
    Observable<StoryDetail> storyDetail(@Path("id") Integer id);

    @GET("start-image/{width}*{height}")
    Observable<StartImage> startImage(@Path("width") Integer width, @Path("height") Integer height);

}
