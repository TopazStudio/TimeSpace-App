package com.flycode.timespace.ui.flexible_items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.flycode.timespace.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractExpandableHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.ISectionable
import eu.davidea.viewholders.ExpandableViewHolder

class PlainHeaderItem(
        val id: Int = 1,
        val title: String = "",
        var entries: Int = 0
) : AbstractExpandableHeaderItem<PlainHeaderItem.ViewHolder, ISectionable<*, *>>() {
    init {
        mHidden = false
        mDraggable = false
        mSelectable = false
    }

    var listener : PlainHeaderItemListener? = null
    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
            holder: ViewHolder?,
            position: Int,
            payloads: MutableList<Any>?) {
        holder?.tv_title?.text = title
        holder?.tv_results_count?.text = entries.toString()
        holder?.main_view?.setOnClickListener {
            if (mExpanded) listener?.onCollapse(position)
            else listener?.onExpand(position)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (other is HeaderItem) {
            val inListItem: HeaderItem =  other
            return this.id == inListItem.id
        }
        return false
    }

    override fun createViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): ViewHolder = ViewHolder(view, adapter)

    override fun getLayoutRes(): Int = R.layout.search_result_list_header

    override fun hashCode() : Int = id.hashCode()

    /**
     * The ViewHolder used by this item.
     *
     */
    class ViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : ExpandableViewHolder(view,adapter,true) {
        val main_view: LinearLayout? = view?.findViewById(R.id.main_view)
        val tv_title: TextView? = view?.findViewById(R.id.tv_title)
        val tv_results_count: TextView? = view?.findViewById(R.id.tv_results_count)
    }

    interface PlainHeaderItemListener{
        fun onExpand(position: Int)
        fun onCollapse(position: Int)
    }
}