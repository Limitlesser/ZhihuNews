package wind.zhihunews.service.impl;

import org.greenrobot.greendao.query.WhereCondition;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wind.zhihunews.bean.News;
import wind.zhihunews.bean.StartImage;
import wind.zhihunews.db.model.DaoSession;
import wind.zhihunews.db.model.Story;
import wind.zhihunews.db.model.StoryDetail;
import wind.zhihunews.db.model.StoryDetailDao;
import wind.zhihunews.db.model.TopStory;
import wind.zhihunews.net.Api;
import wind.zhihunews.perf.StartImagePref;
import wind.zhihunews.service.NewsService;

/**
 * Created by wind on 2016/8/17.
 */
public class NewsServiceImpl implements NewsService {


    Api api;

    DaoSession daoSession;

    StartImagePref startImagePref;

    @Inject
    public NewsServiceImpl(Api api, DaoSession daoSession, StartImagePref startImagePref) {
        this.api = api;
        this.daoSession = daoSession;
        this.startImagePref = startImagePref;
    }

    @Override
    public Observable<News> newsLatest() {
        return api.newsLatest()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        saveNews(news);
                    }
                })
                .onErrorResumeNext(Observable.create(new Observable.OnSubscribe<News>() {
                    @Override
                    public void call(Subscriber<? super News> subscriber) {
                        News news = new News();
                        news.setStories(daoSession.getStoryDao().loadAll());
                        news.setTop_stories(daoSession.getTopStoryDao().loadAll());
                        subscriber.onNext(news);
                    }
                }).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<News> newsBefore(String date) {
        return api.newsBefore(date)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        saveNews(news);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<StoryDetail> storyDetail(Integer id) {
        return daoSession
                .queryBuilder(StoryDetail.class)
                .where(new WhereCondition.PropertyCondition(StoryDetailDao.Properties.Id, "=" + id))
                .rx().unique()
                .filter(new Func1<StoryDetail, Boolean>() {
                    @Override
                    public Boolean call(StoryDetail storyDetail) {
                        return storyDetail != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .mergeWith(
                        api.storyDetail(id)
                                .subscribeOn(Schedulers.io())
                                .doOnNext(new Action1<StoryDetail>() {
                                    @Override
                                    public void call(StoryDetail storyDetail) {
                                        saveStory(storyDetail);
                                    }
                                }))
                .first()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<StartImage> startImage(Integer width, Integer height) {
        return api.startImage(width, height)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<StartImage>() {
                    @Override
                    public void call(StartImage startImage) {
                        startImagePref.setImage(startImage.getImg());
                        startImagePref.setText(startImage.getText());
                    }
                })
                .onErrorReturn(new Func1<Throwable, StartImage>() {
                    @Override
                    public StartImage call(Throwable throwable) {
                        return new StartImage(startImagePref.getText(), startImagePref.getImage());
                    }
                })
                .mergeWith(Observable.create(new Observable.OnSubscribe<StartImage>() {
                    @Override
                    public void call(Subscriber<? super StartImage> subscriber) {
                        StartImage startImage = new StartImage(startImagePref.getText(), startImagePref.getImage());
                        subscriber.onNext(startImage);
                    }
                }).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public String detail2Html(String body) {
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        return html;
    }

    private void saveNews(News news) {
        if (news.getStories() != null) {
            for (Story story : news.getStories()) {
                daoSession.insertOrReplace(story);
            }
        }
        if (news.getTop_stories() != null) {
            for (TopStory topStory : news.getTop_stories()) {
                daoSession.insertOrReplace(topStory);
            }
        }
    }

    private void saveStory(StoryDetail storyDetail) {
        daoSession.insertOrReplace(storyDetail);
    }
}
