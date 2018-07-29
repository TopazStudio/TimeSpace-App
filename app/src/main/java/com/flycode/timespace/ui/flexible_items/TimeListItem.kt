package com.flycode.timespace.ui.flexible_items

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Time
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.IHolder
import eu.davidea.viewholders.FlexibleViewHolder
import java.text.SimpleDateFormat
import java.util.*




/**
 * Where AbstractFlexibleItem implements IFlexible!
 */
class TimeListItem(
        var id: Int = 0,
        var time: Time,
        var context : Context? = null
) : AbstractFlexibleItem<TimeListItem.MyViewHolder>(),
        IHolder<Time> {
    init {
        mDraggable = false
        mSwipeable = false
    }

    private var startTime : Calendar = Calendar.getInstance()
    private var endTime : Calendar = Calendar.getInstance()
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var endTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null
    private var startTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null

    var listener: TimeListItemListener? = null

    override fun getModel(): Time = time

    override fun equals(other: Any?): Boolean {
        if (other is TimeListItem) {
            val inListItem: TimeListItem =  other
            return this.time.id == inListItem.time.id
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
    override fun hashCode() : Int = time.id.hashCode()

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    override fun getLayoutRes():Int = R.layout.time_item_layout

    /**
     * Delegates the creation of the ViewHolder to the time (AutoMap).
     * The infladed view is already provided as well as the Adapter.
     */
    override fun createViewHolder(view: View?,
                                  adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): MyViewHolder? = MyViewHolder(view, adapter)


    /**
     * Set the activity's OnDateSetListener
     */
    private fun setOnDateSetListener(holder: MyViewHolder?) {
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            startTime.set(Calendar.YEAR, year)
            startTime.set(Calendar.MONTH, monthOfYear)
            startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            endTime.set(Calendar.YEAR, year)
            endTime.set(Calendar.MONTH, monthOfYear)
            endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            holder?.et_date?.setText(
                    SimpleDateFormat("dd-M-yyyy", Locale.US).format(startTime.time)
            )
            if (!holder?.et_date?.text.isNullOrEmpty()){
                holder?.et_date?.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
            }else{
                holder?.et_date?.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
            }
        }

    }

    /**
     * Set the activity's OnDateSetListener
     */
    private fun setOnTimeSetListener(holder: MyViewHolder?) {
        //TODO check validity

        startTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startTime.set(Calendar.MINUTE, minute)
            holder?.et_start_time?.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(startTime.time)
            )
            if (!holder?.et_start_time?.text.isNullOrEmpty()){
                holder?.et_start_time?.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
            }else{
                holder?.et_start_time?.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
            }
        }

        endTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endTime.set(Calendar.MINUTE, minute)
            holder?.et_end_time?.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(endTime.time)
            )
            if (!holder?.et_end_time?.text.isNullOrEmpty()){
                holder?.et_end_time?.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
            }else{
                holder?.et_end_time?.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
            }
        }
    }
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
        holder?.tv_index?.text = id.toString()
        setOnDateSetListener(holder)
        setOnTimeSetListener(holder)

        holder?.et_date?.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    startTime.get(Calendar.YEAR),
                    startTime.get(Calendar.MONTH),
                    startTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        holder?.et_start_time?.setOnClickListener {
            TimePickerDialog(context, startTime_TimeSetListener,
                    startTime.get(Calendar.HOUR_OF_DAY),
                    startTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        holder?.et_end_time?.setOnClickListener {
            TimePickerDialog(context, endTime_TimeSetListener,
                    endTime.get(Calendar.HOUR_OF_DAY),
                    endTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        holder?.btn_close?.setOnClickListener {
            listener?.onDeleteTime(this)
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

        val main_view: LinearLayout? = view?.findViewById(R.id.main_view)
        val tv_index: TextView? = view?.findViewById(R.id.tv_index)

        val date_hint: TextView? = view?.findViewById(R.id.date_hint)
        val et_date: EditText? = view?.findViewById(R.id.et_date)

        val start_time_hint: TextView? = view?.findViewById(R.id.start_time_hint)
        val et_start_time: EditText? = view?.findViewById(R.id.et_start_time)

        val end_time_hint: TextView? = view?.findViewById(R.id.end_time_hint)
        val et_end_time: EditText? = view?.findViewById(R.id.et_end_time)

        val btn_close: ImageButton? = view?.findViewById(R.id.btn_close)
    }

    interface TimeListItemListener{
        fun onDeleteTime(timeListItem: TimeListItem)
    }
}