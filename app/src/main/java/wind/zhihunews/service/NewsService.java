package wind.zhihunews.service;

import rx.Observable;
import wind.zhihunews.bean.News;
import wind.zhihunews.bean.StartImage;
import wind.zhihunews.db.model.StoryDetail;

/**
 * Created by wind on 2016/8/17.
 */
public interface NewsService {

    Observable<News> newsLatest();

    Observable<News> newsBefore(String date);

    Observable<StoryDetail> storyDetail(String id);

    Observable<StartImage> startImage(Integer width, Integer height);
}
