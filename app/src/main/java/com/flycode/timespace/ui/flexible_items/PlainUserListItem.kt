package com.flycode.timespace.ui.flexible_items

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.User
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.flexibleadapter.utils.FlexibleUtils
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class PlainUserListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var user: User,
        var context : Context? = null
) : AbstractSectionableItem<PlainUserListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<String>,
        IHolder<User> {

    init {
        mDraggable = false
        mSwipeable = false
    }
    var listener: PlainUserListItemListener? = null
    var position: Int = 0

    override fun getModel(): User = user

    //Todo implement filter algorithm
    override fun filter(constraint: String?): Boolean {
        var found = false
        constraint?.let {
            found = "${user.first_name} ${user.second_name} ${user.surname}".toLowerCase().trim().contains(it)
        }
        return found
    }


    override fun equals(other: Any?): Boolean {
        if (other is PlainUserListItem) {
            val inListItem: PlainUserListItem =  other
            return this.user.id == inListItem.user.id
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
    override fun hashCode() : Int = user.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.plain_user_list_item

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

        if (adapter?.hasFilter()!!) {
            val filter = adapter.getFilter(String::class.java)
            FlexibleUtils.highlightText(
                    holder?.tv_name!!,
                    "${user.first_name} ${user.second_name} ${user.surname}",
                    filter,
                    context?.resources?.getColor(R.color.colorRed)!!
            )
        } else {
            holder?.tv_name?.text = "${user.first_name} ${user.second_name} ${user.surname}"
        }

        if (!user.pictures.isEmpty())
            Picasso.get()
                    .load(user.pictures[0].local_location)
                    .into(holder?.im_picture)
        else holder?.im_picture?.setImageDrawable(
                TextDrawable.builder().buildRound(
                        user.first_name.toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor(user.first_name)
                )
        )

        holder?.main_view?.setOnClickListener {
            listener?.onUserClicked(this)
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
    }

    interface PlainUserListItemListener {
        fun onUserClicked(plainUserListItem: PlainUserListItem)
    }
}