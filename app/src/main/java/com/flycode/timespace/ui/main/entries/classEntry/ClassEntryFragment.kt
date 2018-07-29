package com.flycode.timespace.ui.main.entries.classEntry

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
import com.flycode.timespace.databinding.ClazzEntryBinding
import com.flycode.timespace.databinding.CustomTagsPickerBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.thebluealliance.spectrum.SpectrumDialog
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ClassEntryFragment  
    : BaseFragment<ClassEntryFragment, ClassEntryPresenter, ClassEntryViewModel>(),
        ClassEntryContract.ClassEntryFragment,
        GoogleApiClient.OnConnectionFailedListener {

    private val PERMISSION_REQUEST_CODE = 0
    @Inject override lateinit var viewModel: ClassEntryViewModel
    @Inject lateinit var placeAutocompleteAdapter: PlaceAutocompleteAdapter
    @field: [Inject Named("main_tag_adapter")]
    lateinit var mainClassTagsAdapter: ClassTagsAdapter
    @field: [Inject Named("tag_picker_tag_adapter")]
    lateinit var tagPickerClassTagsAdapter: ClassTagsAdapter
    lateinit var customTagPickerBinding: CustomTagsPickerBinding
    lateinit var customTagPickerDialog: MaterialDialog
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var endTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null
    private var startTime_TimeSetListener: TimePickerDialog.OnTimeSetListener? = null

    lateinit var classEntryBinding: ClazzEntryBinding
    var googleApiClient: GoogleApiClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        classEntryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_class_entry,container,false)
        classEntryBinding.viewModel = viewModel
        classEntryBinding.setLifecycleOwner(this)

        return classEntryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(classEntryBinding.toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Add a class"

        checkInternetPermissions()
        if(googleApiClient == null)
            googleApiClient = GoogleApiClient
                    .Builder(context!!)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(activity!!,this)
                    .build()

        classEntryBinding.etAddress.setAdapter(placeAutocompleteAdapter)

        classEntryBinding.etAddress.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            presenter.onSuggestionItemPicked(adapterView,view1,i,l)
        }

        classEntryBinding.btnAddTag.setOnClickListener {
            customTagPickerDialog.show()
        }

        classEntryBinding.fabDone.setOnClickListener {
            presenter.saveClazz()
        }

        classEntryBinding.fabColor.setOnClickListener {

            val colors : IntArray = IntArray(resources.getStringArray(R.array.tag_colors).size)
            for ( (index,i) in resources.getStringArray(R.array.tag_colors).withIndex()){
                colors[index] = Color.parseColor(i)
            }
            SpectrumDialog.Builder(context!!)
                    .setOnColorSelectedListener { positiveResult, color ->
                        if (positiveResult) {
                            viewModel.uiState.clazz.color = String.format("#%06X", 0xFFFFFF and color)
                            classEntryBinding.fabColor.backgroundTintList = ColorStateList.valueOf(color)
                        }
                    }
                    .setColors(colors)
                    .setTitle(R.string.pick_a_color)
                    .build()
                    .show(fragmentManager,"ColorPicker")
        }

        classEntryBinding.etStartTime.setOnClickListener {
            TimePickerDialog(context, startTime_TimeSetListener,
                    viewModel.startTime.get(Calendar.HOUR_OF_DAY),
                    viewModel.startTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        classEntryBinding.etEndTime.setOnClickListener {
            TimePickerDialog(context, endTime_TimeSetListener,
                    viewModel.endTime.get(Calendar.HOUR_OF_DAY),
                    viewModel.endTime.get(Calendar.MINUTE),
                    false
            ).show()
        }

        setOnTimeSetListener()
        setupWeeklyRepeats()
        setupTagsRecyclerView()
        setupTagsEntry()
    }

    private fun setOnTimeSetListener() {
        //TODO check validity

        startTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.startTime.set(Calendar.MINUTE, minute)
            classEntryBinding.etStartTime.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(viewModel.startTime.time)
            )
            if (!classEntryBinding.etStartTime.text.isNullOrEmpty()){
                classEntryBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
                classEntryBinding.startTimeHint.visibility = View.VISIBLE
            }else{
                classEntryBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                classEntryBinding.startTimeHint.visibility = View.GONE
            }

            if(classEntryBinding.etEndTime.text.isNullOrEmpty()){
                classEntryBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                classEntryBinding.endTimeHint.visibility = View.INVISIBLE
            }
        }

        endTime_TimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.endTime.set(Calendar.MINUTE, minute)
            classEntryBinding.etEndTime.setText(
                    SimpleDateFormat("HH:mm", Locale.US).format(viewModel.endTime.time)
            )
            if (!classEntryBinding.etEndTime.text.isNullOrEmpty()){
                classEntryBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_blue_rounded)
                classEntryBinding.endTimeHint.visibility = View.VISIBLE
            }else{
                classEntryBinding.etEndTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                classEntryBinding.endTimeHint.visibility = View.GONE
            }

            if(classEntryBinding.etStartTime.text.isNullOrEmpty()){
                classEntryBinding.etStartTime.background = context?.resources?.getDrawable(R.drawable.et_lt_gray)
                classEntryBinding.startTimeHint.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupTagsRecyclerView() {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()

        classEntryBinding.chipsRecyclerView.layoutManager = chipsLayoutManager
        classEntryBinding.chipsRecyclerView.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        classEntryBinding.chipsRecyclerView.adapter = mainClassTagsAdapter
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

        tagPickerClassTagsAdapter.onTagClickedListener = object : ClassTagsAdapter.OnTagClickedListener {
            override fun onTagClicked(tag: Tag) {
                presenter.tagClass(tag)
                customTagPickerDialog.hide()
            }
        }
        customTagPickerBinding.chipsRecyclerView.adapter = tagPickerClassTagsAdapter
    }

    private fun setupWeeklyRepeats() {
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.days_of_the_week, R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classEntryBinding.etRepeatsOnSpinner.adapter = adapter

        val initialPosition = if (viewModel.uiState.time.weekly_repeats > 0) viewModel.uiState.time.weekly_repeats - 1 else 0
        classEntryBinding.etRepeatsOnSpinner.setSelection(initialPosition)

        classEntryBinding.etRepeatsOnSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.uiState.time.weekly_repeats = position + 1
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
        fun newInstance(param1: String, param2: String) = ClassEntryFragment()
    }
}
