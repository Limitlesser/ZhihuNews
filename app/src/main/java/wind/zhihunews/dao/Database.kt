package wind.zhihunews.dao

import android.database.sqlite.SQLiteDatabase
import android.support.v4.util.LruCache
import wind.zhihunews.util.getOrPut
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.replace
import java.lang.reflect.Field

private val modelCache = LruCache<Class<*>, Array<Field>>(10)

fun tableName(model: Any): String = model::class.java.simpleName

fun toContentValues(model: Any): Array<Pair<String, *>> {
    return modelCache.getOrPut(model::class.java)
    { model::class.java.declaredFields.apply { forEach { it.isAccessible = true } } }
            .map { it.name to map2Db(it.get(model)) }.toTypedArray()
}

fun map2Db(value: Any): Any {
    return value
}

fun SQLiteDatabase.insert(model: Any) {
    insert(tableName(model), *toContentValues(model))
}

fun SQLiteDatabase.replace(model: Any) {
    replace(tableName(model), *toContentValues(model))
}

fun SQLiteDatabase.insertList(list: List<Any>) = list.forEach { insert(it) }

fun SQLiteDatabase.replaceList(list: List<Any>) = list.forEach { replace(it) }




