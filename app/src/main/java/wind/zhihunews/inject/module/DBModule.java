package wind.zhihunews.inject.module;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wind.zhihunews.db.model.DaoMaster;
import wind.zhihunews.db.model.DaoSession;

/**
 * Created by wind on 2016/8/17.
 */
@Module
public class DBModule {

    public static final String DB_NAME = "zhihu_news";

    @Provides
    @Singleton
    public DaoMaster providerDaoMaster(Database db) {
        return new DaoMaster(db);
    }

    @Provides
    @Singleton
    public DaoSession providerDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    public Database providerDatabase(DaoMaster.DevOpenHelper helper) {
        return helper.getWritableDb();
    }

    @Provides
    @Singleton
    public DaoMaster.DevOpenHelper providerOpenHelper(Application application) {
        return new DaoMaster.DevOpenHelper(application, DB_NAME);
    }


}
