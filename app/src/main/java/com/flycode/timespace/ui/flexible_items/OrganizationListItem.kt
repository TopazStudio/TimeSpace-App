package com.flycode.timespace.ui.flexible_items

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Organization
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.viewholders.FlexibleViewHolder
import java.io.Serializable

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class OrganizationListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var organization: Organization
) : AbstractSectionableItem<OrganizationListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*,*>>>(header),
        IFilterable<Serializable>,
        IHolder<Organization> {

    init {
        mDraggable = false
        mSwipeable = false
    }

    var listener: OrganizationListItemListener? = null

    override fun getModel(): Organization = organization

    //Todo implement filter algorithm
    override fun filter(constraint: Serializable?): Boolean {
        return organization.name.contains(constraint.toString())
    }


    override fun equals(other: Any?): Boolean {
        if (other is OrganizationListItem) {
            val inListItem: OrganizationListItem =  other
            return this.organization.id == inListItem.organization.id
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
    override fun hashCode() : Int = organization.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.organization_list_item

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
        holder?.tv_name?.text = organization.name
        holder?.tv_description?.text = organization.description
//        holder?.tv_member_since?.text = organization.description

        if (!organization.pictures.isEmpty())
            Picasso.get()
                    .load(organization.pictures[0].remote_location)
                    .into(holder?.im_picture)
        else holder?.im_picture?.setImageDrawable(
                TextDrawable.builder().buildRound(
                        organization.name.toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor(organization.name)
                )
        )

        holder?.main_view?.setOnClickListener {
            listener?.onOrganizationClicked(organization)
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
        val tv_member_since: TextView? = view?.findViewById(R.id.tv_member_since)
        val tv_description: TextView? = view?.findViewById(R.id.tv_description)
    }

    interface OrganizationListItemListener {
        fun onOrganizationClicked(organization: Organization)
    }
}