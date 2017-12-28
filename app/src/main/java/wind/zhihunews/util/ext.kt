@file:Suppress("NOTHING_TO_INLINE")

package wind.zhihunews.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.util.LruCache
import android.util.SparseArray
import org.jetbrains.anko.AnkoContext
import java.util.*


inline fun <K, V> LruCache<K, V>.getOrPut(key: K, init: () -> V): V =
        this[key] ?: init().apply { put(key, this) }

inline operator fun <E> SparseArray<E>.set(key: Int, value: E) = put(key, value)

inline fun <E> MutableList<E>.swap(from: Int, to: Int) {
    Collections.swap(this, from, to)
}

inline fun Context.drawable(@DrawableRes id: Int): Drawable =
        ContextCompat.getDrawable(this, id)!!

inline fun <T> AnkoContext<T>.drawable(@DrawableRes id: Int) = ctx.drawable(id)

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, Observer { observer(it!!) })
}
