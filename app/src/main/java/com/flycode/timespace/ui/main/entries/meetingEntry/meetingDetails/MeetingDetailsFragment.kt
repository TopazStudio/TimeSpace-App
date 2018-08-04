package com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.afollestad.materialdialogs.MaterialDialog
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Tag
import com.flycode.timespace.databinding.CustomTagsPickerBinding
import com.flycode.timespace.databinding.MeetingsDetailsBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.main.entries.classEntry.PlaceAutocompleteAdapter
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryFragment
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.thebluealliance.spectrum.SpectrumDialog
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MeetingDetailsFragment  
    : BaseFragment<MeetingDetailsFragment, MeetingDetailsPresenter, MeetingDetailsViewModel>(),
        MeetingDetailsContract.MeetingDetailsFragment,
        MeetingEntryFragment.MeetingEntryFragmentInterface,
        GoogleApiClient.OnConnectionFailedListener {

    @Inject override lateinit var viewModel: MeetingDetailsViewModel
    @Inject lateinit var superViewModel: MeetingEntryViewModel
    lateinit var meetingsDetailsBinding: MeetingsDetailsBinding
    @Inject lateinit var placeAutocompleteAdapter: PlaceAutocompleteAdapter
    @field: [Inject Named("main_tag_adapter")]
    lateinit var mainClassTagsAdapter: MeetingTagsAdapter
    @field: [Inject Named("tag_picker_tag_adapter")]
    lateinit var tagPickerClassTagsAdapter: MeetingTagsAdapter
    lateinit var customTagPickerBinding: CustomTagsPickerBinding
    lateinit var customTagPickerDialog: MaterialDialog
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var endTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null
    private var startTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null
    var googleApiClient: GoogleApiClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        meetingsDetailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_details,container,false)
        meetingsDetailsBinding.superViewModel = superViewModel
        meetingsDetailsBinding.viewModel = viewModel
        meetingsDetailsBinding.setLifecycleOwner(this)

        return meetingsDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(googleApiClient == null)
            googleApiClient = GoogleApiClient
                    .Builder(context!!)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(activity!!,this)
                    .build()

        meetingsDetailsBinding.etAddress.setAdapter(placeAutocompleteAdapter)
        meetingsDetailsBinding.etAddress.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            presenter.onSuggestionItemPicked(adapterView,view1,i,l)
        }

        meetingsDetailsBinding.btnAddTag.setOnClickListener {
            customTagPickerDialog.show()
        }

        meetingsDetailsBinding.etDate.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    superViewModel.startTime.get(Calendar.YEAR),
                    superViewModel.startTime.get(Calendar.MONTH),
                    superViewModel.startTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        meetingsDetailsBinding.etStartTime.setOnClickListener {
            TimePickerDialog(context, startTime_TimeSetListener,
                    superViewModel.startTime.get(Calendar.HOUR_OF_DAY),
                    superViewModel.startTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        meetingsDetailsBinding.etEndTime.setOnClickListener {
            TimePickerDialog(context, endTime_TimeSetListener,
                    superViewModel.endTime.get(Calendar.HOUR_OF_DAY),
                    superViewModel.endTime.get(Calendar.MINUTE),
                    false
            ).show()
        }
        
        setupColorPicker()
        setOnDateSetListener()
        setOnTimeSetListener()
        setupTagsRecyclerView()
        setupTagsEntry()
    }

    private fun setupColorPicker(){
        meetingsDetailsBinding.fabColor.setOnClickListener {

            val colors : IntArray = IntArray(resources.getStringArray(R.array.tag_colors).size)
            for ( (index,i) in resources.getStringArray(R.array.tag_colors).withIndex()){
                colors[index] = Color.parseColor(i)
            }
            SpectrumDialog.Builder(context!!)
                    .setOnColorSelectedListener { positiveResult, color ->
                        if (positiveResult) {
                            superViewModel.uiState.meeting.color =  String.format("#%06X", 0xFFFFFF and color)
                            meetingsDetailsBinding.fabColor.backgroundTintList = ColorStateList.valueOf(color)
                        }
                    }
                    .setColors(colors)
                    .setTitle(R.string.pick_a_color)
                    .build()
                    .show(fragmentManager,"ColorPicker")
        }

    }

    private fun setOnDateSetListener() {
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            superViewModel.startTime.set(Calendar.YEAR, year)
            superViewModel.startTime.set(Calendar.MONTH, monthOfYear)
            superViewModel.startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            superViewModel.endTime.set(Calendar.YEAR, year)
            superViewModel.endTime.set(Calendar.MONTH, monthOfYear)
            superViewModel.endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            meetingsDetailsBinding.etDate.setText(
                    SimpleDateFormat("dd-M-yyyy", Locale.US).format(superViewModel.startTime.time)
            )
            if (!meetingsDetailsBinding.etDate.text.isNullOrEmpty()){
                meetingsDetailsBinding.etDate.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
            }else{
                meetingsDetailsBinding.etDate.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
            }
        }

    }

    private fun setOnTimeSetListener() {

        startTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            superViewModel.startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            superViewModel.startTime.set(Calendar.MINUTE, minute)
            meetingsDetailsBinding.etStartTime.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(superViewModel.startTime.time)
            )
            if (!meetingsDetailsBinding.etStartTime.text.isNullOrEmpty()){
                meetingsDetailsBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
                meetingsDetailsBinding.startTimeHint.visibility = View.VISIBLE
            }else{
                meetingsDetailsBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                meetingsDetailsBinding.startTimeHint.visibility = View.GONE
            }

            if(meetingsDetailsBinding.etEndTime.text.isNullOrEmpty()){
                meetingsDetailsBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                meetingsDetailsBinding.endTimeHint.visibility = View.INVISIBLE
            }
        }

        endTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            superViewModel.endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            superViewModel.endTime.set(Calendar.MINUTE, minute)
            meetingsDetailsBinding.etEndTime.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(superViewModel.endTime.time)
            )
            if (!meetingsDetailsBinding.etEndTime.text.isNullOrEmpty()){
                meetingsDetailsBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
                meetingsDetailsBinding.endTimeHint.visibility = View.VISIBLE
            }else{
                meetingsDetailsBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                meetingsDetailsBinding.endTimeHint.visibility = View.GONE
            }

            if(meetingsDetailsBinding.etStartTime.text.isNullOrEmpty()){
                meetingsDetailsBinding.etStartTime?.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                meetingsDetailsBinding.startTimeHint?.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupTagsRecyclerView() {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        meetingsDetailsBinding.chipsRecyclerView.layoutManager = chipsLayoutManager
        meetingsDetailsBinding.chipsRecyclerView.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        meetingsDetailsBinding.chipsRecyclerView.adapter = mainClassTagsAdapter
    }

    private fun setupTagsEntry(){
        presenter.fetchTags()

        customTagPickerBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.tag_picker_layout, null, false)
        customTagPickerBinding.uiState = viewModel.tagsEntryUiState
        customTagPickerDialog = MaterialDialog.Builder(context!!)
                .customView(customTagPickerBinding.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build()

        customTagPickerBinding.tagsReloadBtn.setOnClickListener {
            presenter.fetchTags()
        }

        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(5)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        customTagPickerBinding.chipsRecyclerView.layoutManager = chipsLayoutManager
        customTagPickerBinding.chipsRecyclerView.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        tagPickerClassTagsAdapter.onTagClickedListener = object : MeetingTagsAdapter.OnTagClickedListener {
            override fun onTagClicked(tag: Tag) {
                presenter.tagMeeting(tag)
                customTagPickerDialog.hide()
            }
        }
        customTagPickerBinding.chipsRecyclerView.adapter = tagPickerClassTagsAdapter
    }

    override fun onResume() {
        super.onResume()
        if(!googleApiClient?.isConnected!!){
            googleApiClient?.connect()
        }
    }

    override fun onPause() {
        super.onPause()
        googleApiClient?.stopAutoManage(activity!!)
        googleApiClient?.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        googleApiClient = null
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        if(p0.errorMessage !=  null){
            showError(p0.errorMessage!!)
        }else{
            showError(resources.getString(R.string.something_went_wrong))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MeetingDetailsFragment()
    }
}
