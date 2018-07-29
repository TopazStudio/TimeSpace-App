package com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Tag
import com.pchmn.materialchips.ChipView
import java.util.*

class MeetingTagsAdapter(private val context: Context)
    : RecyclerView.Adapter<MeetingTagsAdapter.ViewHolder>() {

    val tagList: MutableList<Tag>
    var onTagClickedListener: OnTagClickedListener? = null

    init {
        this.tagList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.tag_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTag = tagList[position]
        //        holder.chip_view.setChip(tagList.get(position));
        holder.chip_view.label = currentTag.name
        holder.chip_view.setLabelColor(Color.WHITE)
        holder.chip_view.setChipBackgroundColor(if (tagList[position].color != 0)
            tagList[position].color else Color.LTGRAY)

        //ON UPDATE TAG
        holder.chip_view.setOnChipClicked {
            onTagClickedListener?.onTagClicked(currentTag)
        }

    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    fun addTag(tag: Tag) {
        if (tagList.add(tag)) {
            notifyItemInserted(tagList.size - 1)
        }
    }

    fun addMultipleTags(tags: List<Tag>) {
        val startPosition = tagList.size //Initial start position before adding.
        if (tagList.addAll(tags)) {
            notifyItemRangeInserted(startPosition, tagList.size)
        }
    }

    fun deleteTag(tag: Tag) {
        val index = tagList.indexOf(tag)
        if (tagList.remove(tag)) {
            notifyItemRemoved(index)
        }
    }

    fun updateTag(tag: Tag, index: Int) {
        tagList.removeAt(index)
        tagList.add(index, tag)
        notifyItemChanged(index)
    }

    fun clear() {
        tagList.clear()
        notifyDataSetChanged()
    }

    interface OnTagClickedListener {
        fun onTagClicked(tag: Tag)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chip_view: ChipView = itemView.findViewById(R.id.chip_view)
    }
}
