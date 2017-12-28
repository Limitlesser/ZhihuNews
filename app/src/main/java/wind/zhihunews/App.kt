package wind.zhihunews

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import wind.zhihunews.dao.DatabaseOpenHelper
import wind.zhihunews.dao.database


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        database = DatabaseOpenHelper(this)
        Fresco.initialize(this)
    }
}