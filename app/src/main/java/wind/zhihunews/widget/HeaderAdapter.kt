package wind.zhihunews.widget

/**
 * Created by wind on 2016/8/18.
 */

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup


import java.util.ArrayList

/**
 * 包装RecyclerView的数据适配器,添加头和尾操作
 * 利用adapter的分类达到
 */
class HeaderAdapter @JvmOverloads constructor(private var mAdapter: RecyclerView.Adapter<*>? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mHeaderViews: ArrayList<HeaderViewItem>
    private val mFootViews: ArrayList<HeaderViewItem>
    private var mHeaderCount: Int = 0
    private var mFooterCount: Int = 0//头/尾的总个数

    val headersCount: Int
        get() = mHeaderViews.size

    val footersCount: Int
        get() = mFootViews.size


    /**
     * 底部组起始位置
     *
     * @return
     */
    val footerStartIndex: Int
        get() {
            var index = headersCount
            if (null != mAdapter) {
                index += mAdapter!!.itemCount
            }
            return index
        }

    init {
        if (mAdapter != null)
            registerAdapterDataObserver(mAdapter)
        this.mHeaderViews = ArrayList()
        this.mFootViews = ArrayList()
    }

    /**
     * 设置当前数据适配器
     *
     * @param adapter
     */
    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        this.mAdapter = adapter
        registerAdapterDataObserver(mAdapter)
        notifyDataSetChanged()
    }

    private fun registerAdapterDataObserver(adapter: RecyclerView.Adapter<*>?) {
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView!!.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isHeader(position) || isFooter(position)) manager.spanCount else 1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder)
        val lp = holder!!.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.layoutPosition) || isFooter(holder.layoutPosition))) {
            lp.isFullSpan = true
        }
    }

    fun isHeader(position: Int): Boolean {
        return position >= 0 && position < mHeaderViews.size
    }

    fun isFooter(position: Int): Boolean {
        val itemCount = itemCount
        return position < itemCount && position >= itemCount - mFootViews.size
    }


    fun addHeaderView(view: View) {
        val viewType = TYPE_HEADER - mHeaderCount
        val index = mHeaderViews.size
        this.mHeaderViews.add(index, HeaderViewItem(viewType, view))
        notifyItemInserted(index)
        mHeaderCount++
    }

    /**
     * 此方法不开放,避免角标混乱
     *
     * @param view
     * @param index
     */
    protected fun addFooterView(view: View, index: Int) {
        var index = index
        //固定第一个
        val viewType = TYPE_FOOTER + mFooterCount
        index = if (index > mFootViews.size) mFootViews.size else index
        this.mFootViews.add(index, HeaderViewItem(viewType, view))//越界处理
        notifyItemInserted(footerStartIndex + index)
        mFooterCount++
    }

    fun addFooterView(view: View) {
        addFooterView(view, mFootViews.size)
    }

    /**
     * 获得指定位置的headerView
     *
     * @param index
     * @return
     */
    fun getHeaderView(index: Int): View? {
        var view: View? = null
        if (0 <= index && index < mHeaderViews.size) {
            view = mHeaderViews[index].view
        }
        return view
    }

    /**
     * 获得指定的位置的footerView
     *
     * @param index
     * @return
     */
    fun getFooterView(index: Int): View? {
        var view: View? = null
        if (0 <= index && index < mFootViews.size) {
            view = mFootViews[index].view
        }
        return view
    }

    /**
     * 移除指定的HeaderView对象
     *
     * @param view
     */
    fun removeHeaderView(view: View?) {
        if (null == view) return
        removeHeaderView(indexOfValue(mHeaderViews, view))
    }


    /**
     * 移除指定的HeaderView对象
     *
     * @param position
     */
    fun removeHeaderView(position: Int) {
        if (0 > position || mHeaderViews.size <= position) return
        mHeaderViews.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * 移除指定的HeaderView对象
     *
     * @param view
     */
    fun removeFooterView(view: View?) {
        if (null == view) return
        removeFooterView(indexOfValue(mFootViews, view))
    }

    /**
     * 移除指定的HeaderView对象
     *
     * @param position
     */
    fun removeFooterView(position: Int) {
        if (0 > position || mFootViews.size <= position) return
        mFootViews.removeAt(position)
        notifyItemRemoved(itemCount - footersCount - position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder: RecyclerView.ViewHolder
        if (TYPE_NORMAL > viewType) {
            holder = object : RecyclerView.ViewHolder(getItemValue(mHeaderViews, viewType)!!) {

            }
        } else if (TYPE_NORMAL_ITEM_COUNT < viewType) {
            holder = object : RecyclerView.ViewHolder(getItemValue(mFootViews, viewType)!!) {

            }
        } else {
            holder = mAdapter!!.onCreateViewHolder(parent, viewType)
        }
        return holder
    }

    private fun indexOfValue(items: ArrayList<HeaderViewItem>, view: View): Int {
        var index = -1
        for (i in items.indices) {
            val viewItem = items[i]
            if (viewItem.view === view) {
                index = i
                break
            }
        }
        return index
    }

    private fun getItemValue(items: ArrayList<HeaderViewItem>, type: Int): View? {
        var view: View? = null
        for (i in items.indices) {
            val viewItem = items[i]
            if (viewItem.viewType == type) {
                view = viewItem.view
                break
            }
        }
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var position = position
        if (!isHeader(position)) {
            position -= headersCount
            if (null != mAdapter && position < mAdapter!!.itemCount) {
                @Suppress("UNCHECKED_CAST")
                (mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>).onBindViewHolder(holder, position)
            }
        }
    }


    override fun getItemCount(): Int {
        var itemCount = headersCount + footersCount
        if (null != mAdapter) {
            itemCount += mAdapter!!.itemCount
        }
        return itemCount
    }


    override fun getItemViewType(position: Int): Int {
        var itemType = TYPE_NORMAL
        if (isHeader(position)) {
            itemType = mHeaderViews[position].viewType//头
        } else if (isFooter(position)) {
            itemType = mFootViews[footersCount - (itemCount - position)].viewType //尾
        } else {
            //子条目类型
            val itemPosition = position - headersCount
            if (mAdapter != null) {
                val adapterCount = mAdapter!!.itemCount
                if (itemPosition < adapterCount) {
                    itemType = mAdapter!!.getItemViewType(itemPosition)
                }
            }
        }
        return itemType
    }

    override fun getItemId(position: Int): Long {
        var position = position
        if (mAdapter != null && position >= headersCount) {
            position = -headersCount
            val itemCount = mAdapter!!.itemCount
            if (position < itemCount) {
                return mAdapter!!.getItemId(position)
            }
        }
        return -1
    }

    class HeaderViewItem(val viewType: Int, val view: View)

    companion object {
        private val TAG = "HeaderAdapter"
        private val TYPE_HEADER = -1//从-1起始开始减
        private val TYPE_NORMAL = 0//默认从0开始
        private val TYPE_NORMAL_ITEM_COUNT = 12//随意取的值,确保装饰Adapter对象不会超过此界即可
        private val TYPE_FOOTER = TYPE_NORMAL_ITEM_COUNT + 1
    }
}
