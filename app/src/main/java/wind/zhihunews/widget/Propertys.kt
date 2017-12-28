package wind.zhihunews.widget

import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.internals.AnkoInternals.NO_GETTER
import kotlin.DeprecationLevel.ERROR

fun noGetter(): Nothing = throw IllegalAccessException("no getter")

inline var SimpleDraweeView.imageUriString: String
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) = setImageURI(value)

inline var View.transitionNameCompat
    get() = ViewCompat.getTransitionName(this)
    set(value) = ViewCompat.setTransitionName(this, value)

inline var AppCompatActivity.supportToolbar: Toolbar
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) = setSupportActionBar(value)

inline var HeaderAdapter.adapter: RecyclerView.Adapter<*>
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) = setAdapter(value)
