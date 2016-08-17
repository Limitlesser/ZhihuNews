package wind.zhihunews.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wind.zhihunews.db.model.DaoSession;
import wind.zhihunews.net.Api;
import wind.zhihunews.service.NewsService;
import wind.zhihunews.service.impl.NewsServiceImpl;

/**
 * Created by wind on 2016/8/17.
 */
@Module
public class ServiceModule {

    @Provides
    @Singleton
    public NewsService providerNewsService(Api api, DaoSession daoSession) {
        return new NewsServiceImpl(api, daoSession);
    }

}
