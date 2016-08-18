package wind.zhihunews.inject.component;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import wind.zhihunews.base.BaseActivity;
import wind.zhihunews.binding.BindingActivity;
import wind.zhihunews.inject.module.ApplicationModule;
import wind.zhihunews.ui.MainActivity;
import wind.zhihunews.ui.SplashActivity;

/**
 * Created by wind on 2016/8/17.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface AppComponent {


    Application getApplication();

    void inject(SplashActivity activity);

    void inject(MainActivity mainActivity);


    class Instance {
        private static AppComponent sComponent;

        public static void init(@NonNull Application application) {
            sComponent = DaggerAppComponent.builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }

        public static AppComponent get() {
            return sComponent;
        }
    }
}
