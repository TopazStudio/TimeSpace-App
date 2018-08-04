package com.flycode.timespace.ui.main.entries.examEntry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Tag
import com.flycode.timespace.databinding.CustomTagsPickerBinding
import com.flycode.timespace.databinding.ExamEntryBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.thebluealliance.spectrum.SpectrumDialog
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ExamEntryFragment  
    : BaseFragment<ExamEntryFragment, ExamEntryPresenter, ExamEntryViewModel>(),
        ExamEntryContract.ExamEntryFragment, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    override lateinit var viewModel: ExamEntryViewModel
    @Inject
    lateinit var placeAutocompleteAdapter: PlaceAutocompleteAdapter
    @field: [Inject Named("main_tag_adapter")]
    lateinit var mainClassTagsAdapter: ExamTagsAdapter
    @field: [Inject Named("tag_picker_tag_adapter")]
    lateinit var tagPickerClassTagsAdapter: ExamTagsAdapter
    lateinit var customTagPickerBinding: CustomTagsPickerBinding
    lateinit var customTagPickerDialog: MaterialDialog
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var endTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null
    private var startTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null

    lateinit var examEntryBinding: ExamEntryBinding
    var googleApiClient: GoogleApiClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        examEntryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_exam_entry,container,false)
        examEntryBinding.viewModel = viewModel
        examEntryBinding.setLifecycleOwner(this)

        return examEntryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(examEntryBinding.toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Add an exam"

        checkInternetPermissions()
        if(googleApiClient == null)
            googleApiClient = GoogleApiClient
                    .Builder(context!!)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(activity!!,this)
                    .build()

        examEntryBinding.etAddress.setAdapter(placeAutocompleteAdapter)

        examEntryBinding.etAddress.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            presenter.onSuggestionItemPicked(adapterView,view1,i,l)
        }

        examEntryBinding.btnAddTag.setOnClickListener {
            customTagPickerDialog.show()
        }

        examEntryBinding.fabDone.setOnClickListener {
            presenter.saveExam()
        }

        examEntryBinding.fabColor.setOnClickListener {

            val colors : IntArray = IntArray(resources.getStringArray(R.array.tag_colors).size)
            for ( (index,i) in resources.getStringArray(R.array.tag_colors).withIndex()){
                colors[index] = Color.parseColor(i)
            }
            SpectrumDialog.Builder(context!!)
                    .setOnColorSelectedListener { positiveResult, color ->
                        if (positiveResult) {
                            viewModel.uiState.examination.color = String.format("#%06X", 0xFFFFFF and color)
                            examEntryBinding.fabColor.backgroundTintList = ColorStateList.valueOf(color)
                        }
                    }
                    .setColors(colors)
                    .setTitle(R.string.pick_a_color)
                    .build()
                    .show(fragmentManager,"ColorPicker")
        }

        examEntryBinding.etDate.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    viewModel.startTime.get(Calendar.YEAR),
                    viewModel.startTime.get(Calendar.MONTH),
                    viewModel.startTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        examEntryBinding.etStartTime.setOnClickListener {
            TimePickerDialog(context, startTime_TimeSetListener,
                    viewModel.startTime.get(Calendar.HOUR_OF_DAY),
                    viewModel.startTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        examEntryBinding.etEndTime.setOnClickListener {
            TimePickerDialog(context, endTime_TimeSetListener,
                    viewModel.endTime.get(Calendar.HOUR_OF_DAY),
                    viewModel.endTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        setupExamTypeSpinner()
        setOnDateSetListener()
        setOnTimeSetListener()
        setupTagsRecyclerView()
        setupTagsEntry()
    }

    private fun setOnDateSetListener() {
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.startTime.set(Calendar.YEAR, year)
            viewModel.startTime.set(Calendar.MONTH, monthOfYear)
            viewModel.startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            viewModel.endTime.set(Calendar.YEAR, year)
            viewModel.endTime.set(Calendar.MONTH, monthOfYear)
            viewModel.endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            examEntryBinding.etDate.setText(
                    SimpleDateFormat("dd-M-yyyy", Locale.US).format(viewModel.startTime.time)
            )
            if (!examEntryBinding.etDate.text.isNullOrEmpty()){
                examEntryBinding.etDate.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
            }else{
                examEntryBinding.etDate.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
            }
        }

    }

    private fun setOnTimeSetListener() {
        //TODO check validity

        startTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.startTime.set(Calendar.MINUTE, minute)
            examEntryBinding.etStartTime.setText(
                    SimpleDateFormat("hh:mm a", Locale.US).format(viewModel.startTime.time)
            )
            if (!examEntryBinding.etStartTime.text.isNullOrEmpty()){
                examEntryBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
                examEntryBinding.startTimeHint.visibility = View.VISIBLE
            }else{
                examEntryBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                examEntryBinding.startTimeHint.visibility = View.GONE
            }

            if(examEntryBinding.etEndTime.text.isNullOrEmpty()){
                examEntryBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                examEntryBinding.endTimeHint.visibility = View.INVISIBLE
            }
        }

        endTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.endTime.set(Calendar.MINUTE, minute)
            examEntryBinding.etEndTime.setText(
                    SimpleDateFormat("hh:mm a", Locale.US).format(viewModel.endTime.time)
            )
            if (!examEntryBinding.etEndTime.text.isNullOrEmpty()){
                examEntryBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
                examEntryBinding.endTimeHint.visibility = View.VISIBLE
            }else{
                examEntryBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                examEntryBinding.endTimeHint.visibility = View.GONE
            }

            if(examEntryBinding.etStartTime.text.isNullOrEmpty()){
                examEntryBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                examEntryBinding.startTimeHint.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupTagsRecyclerView() {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        examEntryBinding.chipsRecyclerView.layoutManager = chipsLayoutManager
        examEntryBinding.chipsRecyclerView.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        examEntryBinding.chipsRecyclerView.adapter = mainClassTagsAdapter
    }

    private fun setupTagsEntry(){
        presenter.fetchTags()

        customTagPickerBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.tag_picker_layout, null, false)

        customTagPickerDialog = MaterialDialog.Builder(context!!)
                .customView(customTagPickerBinding.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build()

        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        customTagPickerBinding.chipsRecyclerView.layoutManager = chipsLayoutManager
        customTagPickerBinding.chipsRecyclerView.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        tagPickerClassTagsAdapter.onTagClickedListener = object : ExamTagsAdapter.OnTagClickedListener {
            override fun onTagClicked(tag: Tag) {
                presenter.tagClass(tag)
                customTagPickerDialog.hide()
            }
        }
        customTagPickerBinding.chipsRecyclerView.adapter = tagPickerClassTagsAdapter
    }

    private fun setupExamTypeSpinner() {
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.exam_types, R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        examEntryBinding.examTypeSpinner.adapter = adapter

        examEntryBinding.examTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.uiState.examination.type = resources.getStringArray(R.array.exam_types)[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
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

    private fun checkInternetPermissions() {
        requestAppPermissions(
                arrayOf(
                        android.Manifest.permission.INTERNET
                ),
                R.string.we_need_permission_to_function, PERMISSION_REQUEST_CODE)
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
        fun newInstance(param1: String, param2: String) = ExamEntryFragment()
    }
}
