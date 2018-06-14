package com.flycode.timespace.ui.flexible_items

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Clazz
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class ClassListItem(
        var clazz: Clazz
) : AbstractFlexibleItem<ClassListItem.MyViewHolder>() {

    init {
        mEnabled = true
        isHidden = false
        mSelectable = true
        mDraggable = false
        mSwipeable = true
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement or use
     * default java implementation (return this == o;) if you don't have unique IDs!
     * This will be explained in the "Item interfaces" Wiki page.
     */

    override fun equals(other: Any?): Boolean {
        if (other is ClassListItem) {
            val inListItem: ClassListItem =  other
            return this.clazz.id == inListItem.clazz.id
        }
        return false
    }

    /**
     * You should implement also this method if equals() is implemented.
     * This method, if implemented, has several implications that Adapter handles better:
     * - The Hash, increases performance in big list during Update & Filter operations.
     * - You might want to activate stable ids via Constructor for RV, if your id
     *   is unique (read more in the wiki page: "Setting Up Advanced") you will benefit
     *   of the animations also if notifyDataSetChanged() is invoked.
     */
    override fun hashCode() : Int = clazz.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.meeting_list_item

    /**
     * Delegates the creation of the ViewHolder to the user (AutoMap).
     * The infladed view is already provided as well as the Adapter.
     */

    override fun createViewHolder(view: View?,
                                  adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): MyViewHolder? = MyViewHolder(view, adapter)

    /**
     * The Adapter and the Payload are provided to perform and get more specific
     * information.
     */
    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
            holder: MyViewHolder?,
            position: Int,
            payloads: MutableList<Any>?
    ) {
        holder?.tv_name?.text = clazz.name
    }

    /**
     * The ViewHolder used by this item.
     *
     */
    class MyViewHolder(
            view: View? ,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : FlexibleViewHolder(view,adapter) {

        val tv_abbr: TextView? = view?.findViewById(R.id.tv_abbr)
        val tv_name: TextView? = view?.findViewById(R.id.tv_name)
        val tv_members: TextView? = view?.findViewById(R.id.tv_members)
        val tv_accepted: TextView? = view?.findViewById(R.id.tv_accepted)
        val tv_start_time: TextView? = view?.findViewById(R.id.tv_start_time)
        val tv_end_time: TextView? = view?.findViewById(R.id.tv_end_time)
        val tv_time_table: TextView? = view?.findViewById(R.id.tv_time_table)

    }
}