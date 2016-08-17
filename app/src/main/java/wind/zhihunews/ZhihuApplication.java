package wind.zhihunews;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import wind.zhihunews.image.ImageLoaderUtil;
import wind.zhihunews.inject.component.AppComponent;

/**
 * Created by wind on 2016/8/16.
 */
public class ZhihuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent.Instance.init(this);
        ImageLoaderUtil.initImageLoader(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
