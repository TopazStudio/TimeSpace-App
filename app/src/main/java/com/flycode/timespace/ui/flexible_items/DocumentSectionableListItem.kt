package com.flycode.timespace.ui.flexible_items

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Document
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.*
import eu.davidea.viewholders.FlexibleViewHolder
import java.io.File

/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class DocumentSectionableListItem(
        header : AbstractExpandableHeaderItem<*, ISectionable<*, *>>,
        var document: Document,
        var context : Context? = null,
        val bitmap: Bitmap? = null
) : AbstractSectionableItem<DocumentSectionableListItem.MyViewHolder, AbstractExpandableHeaderItem<*, ISectionable<*, *>>>(header),
        IFilterable<String>,
        IHolder<Document> {

    init {
        mEnabled = true
        isHidden = false
        mSelectable = true
        mDraggable = false
        mSwipeable = false
    }

    override fun getModel(): Document = document

    override fun filter(constraint: String?): Boolean {
        return document.name.contains(constraint.toString())
    }


    override fun equals(other: Any?): Boolean {
        if (other is DocumentSectionableListItem) {
            val inListItem: DocumentSectionableListItem =  other
            return this.document.id == inListItem.document.id
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
    override fun hashCode() : Int = document.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.document_list_item

    /**
     * Delegates the creation of the ViewHolder to the Document (AutoMap).
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

        holder?.tv_name?.text = document.name
        holder?.tv_size?.text = document.size.toString()
        holder?.tv_size_units?.text = context?.getString(R.string.bytes)

        when (document.type){
            "jpg","gif","png","jpeg" -> {
                if (document.local_location.isNullOrEmpty() && bitmap != null)
                    holder?.im_picture?.setImageBitmap(bitmap)
                else
                    Picasso.get()
                            .load(Uri.fromFile(File(document.local_location)))
                            .error(R.drawable.image_placeholder)
                            .placeholder(R.drawable.image_placeholder)
                            .into(holder?.im_picture)
            }
            "zip" -> {
                Picasso.get()
                        .load(R.drawable.zip)
                        .error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder?.im_picture)
            }
            "pdf" -> {
                Picasso.get()
                        .load(R.drawable.pdf)
                        .error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder?.im_picture)
            }
            "xml" -> {
                Picasso.get()
                        .load(R.drawable.xml)
                        .error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder?.im_picture)
            }
            "doc" -> {
                Picasso.get()
                        .load(R.drawable.doc)
                        .error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder?.im_picture)
            }
            "ppt" -> {
                Picasso.get()
                        .load(R.drawable.ppt)
                        .error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder?.im_picture)
            }
            else -> {
                Picasso.get()
                        .load(R.drawable.image_placeholder)
                        .into(holder?.im_picture)
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
        val tv_size: TextView? = view?.findViewById(R.id.tv_time_table)
        val tv_size_units: TextView? = view?.findViewById(R.id.tv_building)
    }

}