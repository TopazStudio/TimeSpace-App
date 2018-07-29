package com.flycode.timespace.ui.flexible_items

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Examination
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.viewholders.FlexibleViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class ExaminationSectionableItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var examination: Examination,
        var context : Context? = null
) : AbstractSectionableItem<ExaminationSectionableItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<String>,
        IHolder<Examination> {

    init {
        mEnabled = true
        isHidden = false
        mSelectable = true
        mDraggable = false
        mSwipeable = false
    }

    override fun getModel(): Examination = examination

    override fun filter(constraint: String?): Boolean {
        return examination.name.contains(constraint.toString())
    }


    override fun equals(other: Any?): Boolean {
        if (other is ExaminationSectionableItem) {
            val inListItem: ExaminationSectionableItem =  other
            return this.examination.id == inListItem.examination.id
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
    override fun hashCode() : Int = examination.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.examination_list_item

    /**
     * Delegates the creation of the ViewHolder to the examination (AutoMap).
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

        holder?.tv_name?.text = examination.name
        holder?.im_picture?.setImageDrawable(
                TextDrawable.builder().buildRound(
                        examination.type,
                        ColorGenerator.MATERIAL.getColor(examination.name)
                )
        )
        holder?.tv_start_time?.text = SimpleDateFormat("hh:mm a", Locale.US).format(Date(examination.time?.start_time!!))
        holder?.tv_end_time?.text = SimpleDateFormat("hh:mm a", Locale.US).format(Date(examination.time?.end_time!!))

        holder?.tv_building?.text = examination.location?.building
        holder?.tv_floor?.text = examination.location?.floor
        holder?.tv_room?.text = examination.location?.room

        if (!examination.tags.isEmpty()){
//            //show some tag colors
//            holder?.tag_colors_frame?.addView(ImageView(context).apply {
//                this.width = 30
//                this.background = tag.g
//            })
        }else holder?.tag_colors_frame?.visibility = View.GONE



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
        val tv_time_table: TextView? = view?.findViewById(R.id.tv_time_table)
        val tv_building: TextView? = view?.findViewById(R.id.tv_building)
        val tv_floor: TextView? = view?.findViewById(R.id.tv_floor)
        val tv_room: TextView? = view?.findViewById(R.id.tv_room)
        val tv_start_time: TextView? = view?.findViewById(R.id.tv_start_time)
        val tv_end_time: TextView? = view?.findViewById(R.id.tv_end_time)

        val tag_colors_frame: LinearLayout? = view?.findViewById(R.id.tag_colors_frame)
    }

}