package wind.zhihunews.inject.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wind.zhihunews.perf.StartImagePref;

/**
 * Created by abelianwang on 2016/8/17.
 */
@Module
public class PrefModule {

    @Provides
    @Singleton
    public StartImagePref providerStartImagePref(Application application) {
        return new StartImagePref(application);
    }

}
