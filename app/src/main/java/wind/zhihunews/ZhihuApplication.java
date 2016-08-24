package wind.zhihunews;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import wind.zhihunews.inject.component.AppComponent;

/**
 * Created by wind on 2016/8/16.
 */
public class ZhihuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent.Instance.init(this);
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
