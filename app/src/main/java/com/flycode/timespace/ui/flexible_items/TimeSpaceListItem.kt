package com.flycode.timespace.ui.flexible_items

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Location
import com.flycode.timespace.data.models.Time
import com.flycode.timespace.ui.main.entries.classEntry.PlaceAutocompleteAdapter
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
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
class TimeSpaceListItem(
        var id: Int = 0,
        var time: Time,
        var location: Location,
        var context: Context? = null,
        val placeAutocompleteAdapter: PlaceAutocompleteAdapter,
        val googleApiClient: GoogleApiClient
) : AbstractFlexibleItem<TimeSpaceListItem.MyViewHolder>(),
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

    var listener: TimeSpaceListItemListener? = null

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
    override fun getLayoutRes():Int = R.layout.timespace_item_layout

    /**
     * Delegates the creation of the ViewHolder to the time (AutoMap).
     * The infladed view is already provided as well as the Adapter.
     */
    override fun createViewHolder(view: View?,
                                  adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): MyViewHolder? = MyViewHolder(view, adapter)


    private fun setOnTimeSetListener(holder: MyViewHolder?) {
        //TODO check validity

        startTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startTime.set(Calendar.MINUTE, minute)
            holder?.et_start_time?.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(startTime.time)
            )
        }

        endTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endTime.set(Calendar.MINUTE, minute)
            holder?.et_end_time?.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(endTime.time)
            )
        }
    }

    private fun setupWeeklyRepeatsPrefix(holder: MyViewHolder?) {
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.days_of_the_week, R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder?.et_repeats_on_spinner?.adapter = adapter

        val initialPosition = if (time.weekly_repeats > 0) time.weekly_repeats - 1 else 0
        holder?.et_repeats_on_spinner?.setSelection(initialPosition)

        holder?.et_repeats_on_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                time.weekly_repeats = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setupLocationsAutoComplete(holder: MyViewHolder?){
        holder?.et_address?.setAdapter(placeAutocompleteAdapter)
        holder?.et_address?.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            Places.GeoDataApi.getPlaceById(
                    googleApiClient,
                    placeAutocompleteAdapter.getItem(i)?.placeId
            ).setResultCallback {
                if (!it.status.isSuccess) {
                    listener?.showError(context?.resources?.getString(R.string.something_went_wrong)!!)
                    it.release()
                }

                location.apply {
                    this.address = it[0].address.toString()
                    this.latLng = "${it[0].latLng.latitude},${it[0].latLng.longitude}"
                }
                it.release()
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
        setOnTimeSetListener(holder)
        setupWeeklyRepeatsPrefix(holder)
        setupLocationsAutoComplete(holder)

        /*TODO: fix having to double click in order to get dialogs*/
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
            listener?.onDeleteTime(position)
        }
    }

    fun onDestroy(){

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
        val et_repeats_on_spinner: AppCompatSpinner? = view?.findViewById(R.id.et_repeats_on_spinner)

        val start_time_hint: TextView? = view?.findViewById(R.id.start_time_hint)
        val et_start_time: EditText? = view?.findViewById(R.id.et_start_time)

        val end_time_hint: TextView? = view?.findViewById(R.id.end_time_hint)
        val et_end_time: EditText? = view?.findViewById(R.id.et_end_time)

        val btn_close: ImageButton? = view?.findViewById(R.id.btn_close)

        val et_address: AutoCompleteTextView? = view?.findViewById(R.id.et_address)
        val et_building: EditText? = view?.findViewById(R.id.et_building)
        val et_floor: EditText? = view?.findViewById(R.id.et_floor)
        val et_room: EditText? = view?.findViewById(R.id.et_room)
    }

    interface TimeSpaceListItemListener{
        fun onDeleteTime(position: Int)
        fun showError(string: String)
    }
}