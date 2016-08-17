package wind.zhihunews;

import android.app.Application;
import android.test.ApplicationTestCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wind.zhihunews.bean.News;
import wind.zhihunews.inject.component.DaggerTestComponent;
import wind.zhihunews.inject.component.TestComponent;
import wind.zhihunews.inject.module.ApplicationModule;
import wind.zhihunews.net.Api;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    TestComponent testComponent;

    Api api;


    public ApplicationTest() {
        super(Application.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        testComponent = DaggerTestComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .build();
        api = testComponent.getApi();
    }


    public void testApi() {
        api.newsLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        System.out.print(news);
                    }
                });
    }
}