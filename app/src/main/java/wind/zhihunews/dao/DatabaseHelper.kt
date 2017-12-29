package wind.zhihunews.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

lateinit var database: DatabaseOpenHelper

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "db", version = 1) {


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Story", true,
                "id" to INTEGER + PRIMARY_KEY,
                "body" to TEXT,
                "title" to TEXT,
                "image" to TEXT)
        db.createTable("StoryDetail", true,
                "id" to INTEGER + PRIMARY_KEY,
                "body" to TEXT,
                "title" to TEXT,
                "image" to TEXT,
                "image_source" to TEXT,
                "share_url" to TEXT,
                "css" to TEXT)
        db.createTable("TopStory", true,
                "id" to INTEGER + PRIMARY_KEY,
                "type" to INTEGER,
                "title" to TEXT,
                "image" to TEXT,
                "ga_prefix" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("Story")
        db.dropTable("StoryDetail")
        db.dropTable("TopStory")
        onCreate(db)
    }

}

