package wind.zhihunews.net;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wind.zhihunews.bean.News;

/**
 * Created by wind on 2016/8/16.
 */
public interface Api {

    String BASE_URL = "http://news-at.zhihu.com/api/4/";
    String START_IMAGE = BASE_URL + "start-image/1080*1920";

    @GET("news/latest")
    Observable<News> newsLatest();

    @GET("news/before/{date}")
    Observable<News> newsBefore(@Path("date") String date);

    @GET("news/{id}")
    Observable<String> storyDetail(@Path("id") String id);

}
