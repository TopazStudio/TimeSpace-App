package com.flycode.timespace.ui.flexible_items

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Meeting
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.viewholders.FlexibleViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class MeetingListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var meeting: Meeting,
        var context : Context? = null
): AbstractSectionableItem<MeetingListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<String>,
        IHolder<Meeting> {

    override fun getModel(): Meeting = meeting

    //Todo implement filter algorithm
    override fun filter(constraint: String?): Boolean {
        return meeting.name.contains(constraint.toString())
    }


    override fun equals(other: Any?): Boolean {
        if (other is MeetingListItem) {
            val inListItem: MeetingListItem =  other
            return this.meeting.id == inListItem.meeting.id
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
    override fun hashCode() : Int = meeting.id.hashCode()

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
        holder?.tv_name?.text = meeting.name
        holder?.im_picture?.setImageDrawable(
                TextDrawable.builder().buildRound(
                        meeting.name.toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor(meeting.name)
                )
        )
        holder?.tv_start_time?.text = SimpleDateFormat("hh:mm a", Locale.US).format(Date(meeting.time?.start_time!!))
        holder?.tv_end_time?.text = SimpleDateFormat("hh:mm a", Locale.US).format(Date(meeting.time?.end_time!!))
    }

    /**
     * The ViewHolder used by this item.
     *
     */
     class MyViewHolder(
            view: View? ,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : FlexibleViewHolder(view,adapter) {

        val im_picture: ImageView? = view?.findViewById(R.id.im_picture)
        val tv_name: TextView? = view?.findViewById(R.id.tv_name)
        val tv_members: TextView? = view?.findViewById(R.id.tv_members)
        val tv_start_time: TextView? = view?.findViewById(R.id.tv_start_time)
        val tv_end_time: TextView? = view?.findViewById(R.id.tv_end_time)
        val tv_time_table: TextView? = view?.findViewById(R.id.tv_time_table)

    }
}