package com.flycode.timespace.ui.flexible_items

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Group
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.viewholders.FlexibleViewHolder
import java.io.Serializable

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class GroupListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var group: Group,
        var joinState: String = "NOT_JOINED",
        var context : Context? = null
) : AbstractSectionableItem<GroupListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<Serializable>,
        IHolder<Group> {

    init {
        mDraggable = false
        mSwipeable = false
    }
    var listener: GroupListItemListener? = null
    public var position: Int = 0

    override fun getModel(): Group = group

    //Todo implement filter algorithm
    override fun filter(constraint: Serializable?): Boolean {
        return group.name.contains(constraint.toString())
    }


    override fun equals(other: Any?): Boolean {
        if (other is GroupListItem) {
            val inListItem: GroupListItem =  other
            return this.group.id == inListItem.group.id
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
    override fun hashCode() : Int = group.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.group_list_item

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
        this.position = position
        holder?.tv_name?.text = group.name
        holder?.tv_description?.text = group.description
//        holder?.tv_created?.text = group.description
        holder?.tv_organization?.text = group.organization?.name

        if (!group.pictures.isEmpty())
            Picasso.get()
                    .load(group.pictures[0].remote_location)
                    .into(holder?.im_picture)
        else holder?.im_picture?.setImageDrawable(
                TextDrawable.builder().buildRound(
                        group.name.toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor(group.name)
                )
        )

        holder?.main_view?.setOnClickListener {
            listener?.onGroupClicked(group)
        }

        when(joinState){
            "JOINED" -> {
                holder?.btn_join?.text = context?.resources?.getString(R.string.joined)
                holder?.btn_join?.setBackgroundColor(context?.resources?.getColor(R.color.colorLtGreen)!!)
                holder?.tv_description?.text = context?.resources?.getString(R.string.joined_successfully)
            }
            "PENDING" -> {
                holder?.btn_join?.text = context?.resources?.getString(R.string.pending)
                holder?.btn_join?.setBackgroundColor(context?.resources?.getColor(R.color.colorLtBrown)!!)
                holder?.tv_description?.text = context?.resources?.getString(R.string.request_sent)
            }
            "REJECTED" -> {
                holder?.btn_join?.text = context?.resources?.getString(R.string.rejected)
                holder?.btn_join?.setBackgroundColor(context?.resources?.getColor(R.color.colorLtRed)!!)
                holder?.tv_description?.text = context?.resources?.getString(R.string.request_rejected)
            }
            "NOT_JOINED" -> {
                holder?.btn_join?.text = context?.resources?.getString(R.string.join)
                holder?.btn_join?.setBackgroundColor(context?.resources?.getColor(R.color.colorPrimaryDark)!!)
                holder?.tv_description?.text = group.description
            }
            else -> {
                holder?.btn_join?.setBackgroundColor(context?.resources?.getColor(R.color.colorLtRed)!!)
                holder?.tv_description?.text = context?.resources?.getString(R.string.something_went_wrong)
                holder?.tv_description?.setTypeface(holder.tv_description.typeface, Typeface.ITALIC)
            }
        }

        holder?.btn_join?.setOnClickListener{
            when(joinState){
                "JOINED" -> listener?.onJoinedGroupRemoved(this,holder,position)
                "PENDING" -> listener?.onPendingGroupRemoved(this,holder,position)
                "NOT_JOINED" -> listener?.onJoinGroup(this,holder,position)
            }
        }
    }

    /**
     * The ViewHolder used by this item.
     *
     */
    class MyViewHolder(
            view: View? ,
            adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ) : FlexibleViewHolder(view,adapter) {

        val main_view: CardView? = view?.findViewById(R.id.main_view)
        val im_picture: ImageView? = view?.findViewById(R.id.im_picture)
        val tv_name: TextView? = view?.findViewById(R.id.tv_name)
        val tv_created: TextView? = view?.findViewById(R.id.tv_created)
        val tv_organization: TextView? = view?.findViewById(R.id.tv_organization)
        val tv_description: TextView? = view?.findViewById(R.id.tv_description)
        val join_request_progress_bar: ProgressBar? = view?.findViewById(R.id.join_request_progress_bar)
        val btn_join : Button? = view?.findViewById(R.id.btn_join)
    }

    interface GroupListItemListener {
        fun onGroupClicked(group: Group)
        fun onJoinGroup(groupListItem: GroupListItem, holder: MyViewHolder?,position: Int)
        fun onJoinedGroupRemoved(groupListItem: GroupListItem, holder: MyViewHolder?,position: Int)
        fun onPendingGroupRemoved(groupListItem: GroupListItem, holder: MyViewHolder?, position: Int)
    }
}