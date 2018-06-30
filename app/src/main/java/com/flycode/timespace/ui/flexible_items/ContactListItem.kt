package com.flycode.timespace.ui.flexible_items

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.User
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.viewholders.FlexibleViewHolder
import java.io.Serializable

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class ContactListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var user: User,
        var context : Context? = null
) : AbstractSectionableItem<ContactListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<Serializable>,
        IHolder<User> {

    init {
        mDraggable = false
        mSwipeable = false
    }
    var listener: ContactListItemListener? = null
    var position: Int = 0

    override fun getModel(): User = user

    //Todo implement filter algorithm
    override fun filter(constraint: Serializable?): Boolean {
        return user.first_name.contains(constraint.toString())
    }


    override fun equals(other: Any?): Boolean {
        if (other is ContactListItem) {
            val inListItem: ContactListItem =  other
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
    override fun getLayoutRes():Int = R.layout.contact_list_item

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
        holder?.tv_name?.text = user.first_name

        val email = user.email.split(";").filter { !it.isEmpty() }
        holder?.tv_email?.text = email
                .reduce { acc, s ->
                    acc + "\n" + s
                }

        try {

            val tel = user.tel.split(";").filter { !it.isEmpty() }
            holder?.tv_tel?.text = tel
                    .reduce { acc, s ->
                        acc + "\n" + s
                    }
        }catch (e: Exception){
            holder?.tv_tel?.text = context?.getString(R.string.no_number)
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
            listener?.onContactClicked(user)
        }

        when(user._tag){
            "NOT_INVITED" -> {
                holder?.btn_follow_or_invite?.text = context?.resources?.getString(R.string.invite)
                holder?.btn_follow_or_invite?.background = context?.resources?.getDrawable(R.drawable.primary_button)
                holder?.btn_follow_or_invite?.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                )
                holder?.btn_follow_or_invite?.setOnClickListener{
                    listener?.onInvite(this,holder,position)
                }
            }

            "NOT_FOLLOWING" -> {
                holder?.btn_follow_or_invite?.text = context?.resources?.getString(R.string.follow)
                holder?.btn_follow_or_invite?.background = context?.resources?.getDrawable(R.drawable.primary_button)
                holder?.btn_follow_or_invite?.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                )
                holder?.btn_follow_or_invite?.setOnClickListener{
                    listener?.onFollow(this,holder,position)
                }
            }

            "FOLLOWING" -> {
                holder?.btn_follow_or_invite?.text = context?.resources?.getString(R.string.following)
                holder?.btn_follow_or_invite?.background = context?.resources?.getDrawable(R.drawable.success_button)
                holder?.btn_follow_or_invite?.setCompoundDrawablesWithIntrinsicBounds(
                        context?.resources?.getDrawable(R.drawable.ic_done_white_24dp),
                        null,
                        null,
                        null
                )
                holder?.btn_follow_or_invite?.setOnClickListener{
                    listener?.onUnFollow(this,holder,position)
                }
            }

            "INVITED" -> {
                holder?.btn_follow_or_invite?.text = context?.resources?.getString(R.string.invited)
                holder?.btn_follow_or_invite?.background = context?.resources?.getDrawable(R.drawable.success_button)
                holder?.btn_follow_or_invite?.setCompoundDrawablesWithIntrinsicBounds(
                        context?.resources?.getDrawable(R.drawable.ic_done_white_24dp),
                        null,
                        null,
                        null
                )
                holder?.btn_follow_or_invite?.setOnClickListener{
                    listener?.onUnInvite(this,holder,position)
                }
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
        val tv_tel: TextView? = view?.findViewById(R.id.tv_tel)
        val tv_email: TextView? = view?.findViewById(R.id.tv_email)
        val btn_follow_or_invite : Button? = view?.findViewById(R.id.btn_follow_or_invite)
    }

    interface ContactListItemListener {
        fun onContactClicked(user: User)
        fun onUnInvite(contactListItem: ContactListItem,holder: ContactListItem.MyViewHolder?,position: Int)
        fun onUnFollow(contactListItem: ContactListItem, holder: MyViewHolder, position: Int)
        fun onFollow(contactListItem: ContactListItem, holder: MyViewHolder, position: Int)
        fun onInvite(contactListItem: ContactListItem, holder: MyViewHolder, position: Int)
    }
}