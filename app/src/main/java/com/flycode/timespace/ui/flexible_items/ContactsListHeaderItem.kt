package com.flycode.timespace.ui.flexible_items

import android.support.v7.widget.AppCompatCheckBox
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

class ContactsListHeaderItem(
        var id: Int,
        var name: String,
        var entries: Int
) : AbstractExpandableHeaderItem
<ContactsListHeaderItem.ViewHolder, ISectionable<*,*>>()
{

    init {
        mHidden = false
        mDraggable = false
        mSelectable = false
        mSwipeable = false
        mExpanded = true
        mEnabled = true
    }
    var listener : ContactsHeaderItemListener? = null

    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
            holder: ViewHolder?,
            position: Int,
            payloads: MutableList<Any>?) {
        holder?.tv_name?.text = name
        holder?.tv_entries?.text = entries.toString()
        holder?.main_view?.setOnClickListener {
            if (mExpanded) listener?.onCollapse(position)
            else listener?.onExpand(position)
        }
        holder?.select_all?.setOnCheckedChangeListener { compoundButton, b ->
            if (b) listener?.onSelectAll(this)
            else listener?.onUnSelectAll(this)
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

    override fun getLayoutRes(): Int = R.layout.contact_list_items_header

    override fun hashCode() : Int = id.hashCode()
    /**
     * The ViewHolder used by this item.
     *
     */
    class ViewHolder(
            view: View?,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : ExpandableViewHolder(view,adapter,true) {
        val tv_name: TextView? = view?.findViewById(R.id.tv_name)
        val main_view: LinearLayout? = view?.findViewById(R.id.main_view)
        val select_all: AppCompatCheckBox? = view?.findViewById(R.id.select_all)
        val tv_entries: TextView? = view?.findViewById(R.id.tv_entries)

    }

    interface ContactsHeaderItemListener{
        fun onExpand(position: Int)
        fun onCollapse(position: Int)
        fun onSelectAll(header: ContactsListHeaderItem)
        fun onUnSelectAll(header: ContactsListHeaderItem)
    }
}