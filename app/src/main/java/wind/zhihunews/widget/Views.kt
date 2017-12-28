package wind.zhihunews.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewManager
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.Holder
import com.facebook.drawee.view.SimpleDraweeView
import wind.zhihunews.base.BaseAdapter
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.UI
import org.jetbrains.anko.custom.ankoView
import wind.zhihunews.R


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.simpleDraweeView() = simpleDraweeView(theme = 0) {}

inline fun ViewManager.simpleDraweeView(theme: Int = 0, init: SimpleDraweeView.() -> Unit): SimpleDraweeView {
    return ankoView({ SimpleDraweeView(it) }, theme = theme, init = init)
}

inline fun <T> ViewManager.convenientBanner(theme: Int = 0, init: ConvenientBanner<T>.() -> Unit)
        : ConvenientBanner<T> {
    return ankoView({ ConvenientBanner(it) }, theme = theme, init = init)
}

fun <T> convenientHolder(init: AnkoContext<Context>.(Subject<T>) -> View): Holder<T> {
    val subject = PublishSubject.create<T>()
    return object : Holder<T> {
        override fun UpdateUI(context: Context, position: Int, data: T) {
            subject.onNext(data)
        }

        override fun createView(context: Context): View {
            return init(AnkoContext.create(context, false), subject)
        }

    }
}

class ConvenientHolderBuilder<T> {
    lateinit var item: (Context, Subject<T>) -> View
}


inline fun <D> RecyclerView.baseAdapter(attachToRecyclerView: Boolean = true,
                                        init: BaseAdapter<D>.() -> Unit): BaseAdapter<D> {
    val adapter = BaseAdapter<D>()
    adapter.init()
    if (attachToRecyclerView)
        this.adapter = adapter
    return adapter
}

inline fun RecyclerView.endlessAdapter(wrapped: RecyclerView.Adapter<*>,
                                       noinline onLoadMore: () -> Unit,
                                       loadView: Int? = null,
                                       keepOnAppending: Boolean = false,
                                       attachToRecyclerView: Boolean = true,
                                       init: EndlessRecyclerViewAdapter.() -> Unit = {})
        : EndlessRecyclerViewAdapter {
    val adapter = EndlessRecyclerViewAdapter(context, wrapped, onLoadMore,
            loadView ?: R.layout.item_loading, keepOnAppending)
    adapter.init()
    if (attachToRecyclerView)
        this.adapter = adapter
    return adapter
}

inline fun RecyclerView.headAdapter(attachToRecyclerView: Boolean = true,
                                    init: HeaderAdapter.() -> Unit): HeaderAdapter {
    val adapter = HeaderAdapter()
    adapter.init()
    if (attachToRecyclerView)
        this.adapter = adapter
    return adapter
}


