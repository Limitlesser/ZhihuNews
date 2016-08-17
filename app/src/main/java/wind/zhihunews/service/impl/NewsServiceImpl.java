package wind.zhihunews.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wind.zhihunews.bean.News;
import wind.zhihunews.bean.StartImage;
import wind.zhihunews.db.model.DaoSession;
import wind.zhihunews.db.model.Story;
import wind.zhihunews.db.model.StoryDetail;
import wind.zhihunews.net.Api;
import wind.zhihunews.service.NewsService;

/**
 * Created by wind on 2016/8/17.
 */
public class NewsServiceImpl implements NewsService {

    Api api;
    DaoSession daoSession;

    @Inject
    public NewsServiceImpl(Api api, DaoSession daoSession) {
        this.api = api;
        this.daoSession = daoSession;
    }

    @Override
    public Observable<News> newsLatest() {
        return api.newsLatest()
                .subscribeOn(Schedulers.io())
                .map(new Func1<News, News>() {
                    @Override
                    public News call(News news) {
                        saveNews(news);
                        return news;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<News> newsBefore(String date) {
        return api.newsBefore(date)
                .subscribeOn(Schedulers.io())
                .map(new Func1<News, News>() {
                    @Override
                    public News call(News news) {
                        saveNews(news);
                        return news;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String lastDate() {
        return null;
    }

    @Override
    public Observable<StoryDetail> storyDetail(String id) {
        return api.storyDetail(id)
                .subscribeOn(Schedulers.io())
                .map(new Func1<StoryDetail, StoryDetail>() {
                    @Override
                    public StoryDetail call(StoryDetail s) {
                        saveStory(s);
                        return s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<StartImage> startImage(Integer width, Integer height) {
        return api.startImage(width, height)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void saveNews(News news) {
        List<Story> stories = new ArrayList<>();
        stories.addAll(news.getStories());
        stories.addAll(news.getTop_stories());
        for (Story story : stories) {
            daoSession.insertOrReplace(story);
        }
    }

    private void saveStory(StoryDetail storyDetail) {
        daoSession.insertOrReplace(storyDetail);
    }
}
