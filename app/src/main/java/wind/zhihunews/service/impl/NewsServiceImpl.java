package wind.zhihunews.service.impl;

import org.greenrobot.greendao.query.WhereCondition;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wind.zhihunews.bean.News;
import wind.zhihunews.bean.StartImage;
import wind.zhihunews.db.model.DaoSession;
import wind.zhihunews.db.model.StoryDetail;
import wind.zhihunews.db.model.StoryDetailDao;
import wind.zhihunews.net.Api;
import wind.zhihunews.perf.StartImagePref;
import wind.zhihunews.service.NewsService;

/**
 * Created by wind on 2016/8/17.
 */
public class NewsServiceImpl implements NewsService {


    private Api api;

    private DaoSession daoSession;

    private StartImagePref startImagePref;

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
                .doOnNext(this::saveNews)
                .onErrorResumeNext(Observable.defer(() -> Observable.just(dbNews())))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<News> newsBefore(String date) {
        return api.newsBefore(date)
                .subscribeOn(Schedulers.io())
                .doOnNext(this::saveNews)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<StoryDetail> storyDetail(Integer id) {
        return api.storyDetail(id)
                .doOnNext(this::saveStory)
                .subscribeOn(Schedulers.io())
                .mergeWith(
                        daoSession
                                .queryBuilder(StoryDetail.class)
                                .where(new WhereCondition.PropertyCondition(StoryDetailDao.Properties.Id, "=" + id))
                                .rx().oneByOne()
                                .filter(storyDetail -> storyDetail != null)
                                .subscribeOn(Schedulers.io())
                )
                .first()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<StartImage> startImage(Integer width, Integer height) {
        return api.startImage(width, height)
                .subscribeOn(Schedulers.io())
                .doOnNext(startImage -> startImagePref.setStartImage(startImage))
                .onErrorReturn(throwable -> startImagePref.getStartImage())
                .mergeWith(Observable.defer(() -> Observable.just(startImagePref.getStartImage())))
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public String detail2Html(String body) {
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        return html;
    }

    private News dbNews() {
        News news = new News();
        news.setStories(daoSession.getStoryDao().loadAll());
        news.setTop_stories(daoSession.getTopStoryDao().loadAll());
        return news;
    }

    private void saveNews(News news) {
        if (news.getStories() != null) {
            daoSession.getStoryDao().insertOrReplaceInTx(news.getStories());
        }
        if (news.getTop_stories() != null) {
            daoSession.getTopStoryDao().insertOrReplaceInTx(news.getTop_stories());
        }
    }

    private void saveStory(StoryDetail storyDetail) {
        daoSession.insertOrReplace(storyDetail);
    }
}
