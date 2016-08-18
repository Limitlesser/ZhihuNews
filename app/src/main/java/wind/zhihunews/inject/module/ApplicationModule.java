package wind.zhihunews.inject.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module(includes = {ApiModule.class, DBModule.class, ServiceModule.class})
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    /**
     * 提供Application单例对象
     *
     * @return Application
     */
    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Context providerContext() {
        return mApplication;
    }
}
