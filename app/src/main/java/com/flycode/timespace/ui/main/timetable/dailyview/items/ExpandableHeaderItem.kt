package com.flycode.timespace.ui.main.timetable.dailyview.items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.flycode.timespace.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractExpandableHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.IHeader
import eu.davidea.flexibleadapter.items.ISectionable
import eu.davidea.viewholders.ExpandableViewHolder
import eu.davidea.viewholders.FlexibleViewHolder

class ExpandableHeaderItem(
        var id: Int,
        var name: String,
        var entries: Int
) : AbstractExpandableHeaderItem<
            ExpandableHeaderItem.ViewHolder,
            ISectionable<RecyclerView.ViewHolder, IHeader<FlexibleViewHolder>>
        >()
{

    init {
        mSelectable = true
    }

    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
            holder: ViewHolder?,
            position: Int,
            payloads: MutableList<Any>?) {
        holder?.tv_name?.text = name
        holder?.tv_entries?.text = entries.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is HeaderItem) {
            val inListItem: HeaderItem  =  other
            return this.id == inListItem.id
        }
        return false
    }

    override fun createViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): ViewHolder = ViewHolder(view,adapter)

    override fun getLayoutRes(): Int = R.layout.daily_list_items_header

    override fun hashCode() : Int = id.hashCode()

    /**
     * The ViewHolder used by this item.
     *
     */
    class ViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : ExpandableViewHolder(view,adapter) {
        val tv_name: TextView? = view?.findViewById(R.id.tv_name)
        val expand_image_view: ImageView? = view?.findViewById(R.id.expand_image_view)
        val view_all: LinearLayout? = view?.findViewById(R.id.view_all)
        val tv_entries: TextView? = view?.findViewById(R.id.tv_entries)

    }
}