package wind.zhihunews.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.service.impl.NewsServiceImpl;

/**
 * Created by wind on 2016/8/17.
 */
@Module
public class ServiceModule {

    @Provides
    @Singleton
    public NewsService providerNewsService(NewsServiceImpl newsService) {
        return newsService;
    }

}
