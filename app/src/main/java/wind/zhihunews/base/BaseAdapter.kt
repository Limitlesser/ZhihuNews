package wind.zhihunews.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.jetbrains.anko.AnkoContext
import wind.zhihunews.util.set
import wind.zhihunews.util.swap


open class BaseAdapter<D> : RecyclerView.Adapter<BaseAdapter<D>.ItemHolder>() {

    private var data = mutableListOf<D>()

    fun setData(data: List<D>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun set(position: Int, data: D) {
        this.data[position] = data
        notifyItemChanged(position)
    }

    fun add(data: D) {
        this.data.add(data)
        notifyItemInserted(this.data.lastIndex - 1)
    }

    fun add(position: Int, data: D) {
        this.data.add(position, data)
        notifyItemInserted(position)
    }

    fun add(data: List<D>) {
        this.data.addAll(data)
        notifyItemRangeInserted(this.data.lastIndex - data.size, data.size)
    }

    fun add(position: Int, data: List<D>) {
        this.data.addAll(position, data)
        notifyItemRangeInserted(position, data.size)
    }

    fun swap(from: Int, to: Int) {
        this.data.swap(from, to)
        notifyItemMoved(from, to)
    }

    fun remove(position: Int, end: Int = position) {
        for (i in end downTo position)
            data.removeAt(i)
        notifyItemRangeRemoved(position, end - position + 1)
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): D = data[position]

    private val itemHolder: SparseArray<((ViewGroup, Observable<D>) -> View)> = SparseArray(1)

    private var itemClick: ((ItemHolder, D, Int) -> Unit)? = null

    fun item(viewType: Int = 0, init: AnkoContext<Context>.(Observable<D>) -> View) {
        itemHolder[viewType] = { parent, dataSource ->
            AnkoContext.create(parent.context).init(dataSource)
        }
    }

    fun itemClick(block: ItemHolder.(D, Int) -> Unit) {
        itemClick = { holder, data, position -> holder.block(data, position) }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val subject = PublishSubject.create<D>()
        val holder = ItemHolder(subject, itemHolder[viewType]!!(parent, subject))
        itemClick?.let {
            holder.itemView.setOnClickListener {
                itemClick?.invoke(holder, data[holder.adapterPosition], holder.adapterPosition)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseAdapter<D>.ItemHolder, position: Int) {
        holder.data.onNext(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ItemHolder(val data: Subject<D>, itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}


