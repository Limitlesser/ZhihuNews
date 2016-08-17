package wind.zhihunews.inject.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import wind.zhihunews.inject.module.ApplicationModule;
import wind.zhihunews.net.Api;

/**
 * Created by wind on 2016/8/17.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface TestComponent {

    Api getApi();


    Application getApplication();

}
