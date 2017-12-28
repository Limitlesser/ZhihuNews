package wind.zhihunews.widget

import android.content.Context
import android.support.annotation.AttrRes
import android.util.TypedValue
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.displayMetrics

fun Context.attr(@AttrRes attr: Int): TypedValue {
    val value = TypedValue()
    theme.resolveAttribute(attr, value, true)
    return value
}

fun Context.attrDimen(@AttrRes attr: Int): Int = attr(attr).getDimension(displayMetrics).toInt()

fun Context.attrRes(@AttrRes attr: Int): Int = attr(attr).resourceId

fun <T> AnkoContext<T>.attrDimen(@AttrRes attr: Int) = ctx.attrDimen(attr)
fun <T> AnkoContext<T>.attrRes(@AttrRes attr: Int) = ctx.attrRes(attr)