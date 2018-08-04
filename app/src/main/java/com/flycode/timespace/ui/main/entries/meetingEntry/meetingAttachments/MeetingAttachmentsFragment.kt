package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.flycode.timespace.R
import com.flycode.timespace.databinding.MeetingAttachmentsBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryFragment
import com.tingyik90.snackprogressbar.SnackProgressBar
import com.tingyik90.snackprogressbar.SnackProgressBarManager
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.utils.Log
import javax.inject.Inject

class MeetingAttachmentsFragment  
    : BaseFragment<MeetingAttachmentsFragment, MeetingAttachmentsPresenter, MeetingAttachmentsViewModel>(),
        MeetingAttachmentsContract.MeetingAttachmentsFragment,
        MeetingEntryFragment.MeetingEntryFragmentInterface,
        PlainHeaderItem.PlainHeaderItemListener {

    @Inject lateinit var mainListAdapter: FlexibleAdapter<PlainHeaderItem>
    @Inject override lateinit var viewModel: MeetingAttachmentsViewModel
    lateinit var meetingAttachmentsBinding: MeetingAttachmentsBinding
    lateinit var customImageEntryDialog: MaterialDialog
    lateinit var customFileTypePrompt: MaterialDialog
    var cameraPermissionGranted = false
    var externalReadWritePermissionGranted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        meetingAttachmentsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_attachments, container, false)
        meetingAttachmentsBinding.viewModel = viewModel
        meetingAttachmentsBinding.setLifecycleOwner(this)
        return meetingAttachmentsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        setupMainRecyclerViews()
        setupCustomFileTypePrompt()
        setupCustomImageEntry()

        meetingAttachmentsBinding.fabAdd.setOnClickListener {
            customFileTypePrompt.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FilePickerConst.REQUEST_CODE -> if (resultCode == RESULT_OK && data != null) {
                presenter.addFilePaths(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS))
            }
            SELECT_PHOTO -> if (resultCode == RESULT_OK && data != null) {
                presenter.onPickerImageResult(data)
            }
            CAPTURE_PHOTO -> if (resultCode == RESULT_OK && data != null) {
                presenter.onCaptureImageResult(data)
            }
        }
    }

    private fun setupMainRecyclerViews(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("MainAdapter")

        val adapter = mainListAdapter

        //HEADERS
        adapter.clear()
        if (viewModel.headingsList.isEmpty()) { //No data in viewModel.
            viewModel.headingsList =  ArrayList<PlainHeaderItem>().apply {
                this.add(DOCUMENTS_LIST_POSITION,PlainHeaderItem(1, "Documents", 0).apply {
                    listener = this@MeetingAttachmentsFragment
                })
                this.add(IMAGES_LIST_POSITION,PlainHeaderItem(2, "Images", 0).apply {
                    listener = this@MeetingAttachmentsFragment
                })
            }
            adapter.addItems(0,viewModel.headingsList)
        }
        else {//viewModel already has data.
            //Replace listener if the fragment was destroyed
            viewModel.headingsList.forEach { it.listener = this@MeetingAttachmentsFragment}
            adapter.addItems(0,viewModel.headingsList)
        }

        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setStickyHeaders(true)
                .expandItemsAtStartUp() //Items must be pre-initialized with expanded=true
                .setAutoCollapseOnExpand(false) //Force closes all others expandable item before expanding a new one
                .setDisplayHeadersAtStartUp(true)
                .setAutoScrollOnExpand(true) //Needs a SmoothScrollXXXLayoutManager
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        meetingAttachmentsBinding.attachmentsRecyclerView.layoutManager = layoutManager
        meetingAttachmentsBinding.attachmentsRecyclerView.setHasFixedSize(true)
        meetingAttachmentsBinding.attachmentsRecyclerView.adapter = adapter

        mainListAdapter.expandAll()
    }

    /**
     * Show custom Chooser dialog allowing to choose method of adding a photo
     */
    private fun setupCustomImageEntry() {
        customImageEntryDialog = MaterialDialog.Builder(context!!)
                .title("Add photo(s)")
                .items(R.array.uploadImage_without_removing)
                .itemsIds(R.array.uploadImage_without_removing_ids)
                .itemsCallback { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            val photoPickerIntent = Intent(Intent.ACTION_PICK)
                            photoPickerIntent.type = "image/*"
                            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                            startActivityForResult(photoPickerIntent, SELECT_PHOTO)
                        }
                        1 -> {
                            val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(photoCaptureIntent, CAPTURE_PHOTO)
                        }
                    }
                }.build()
    }

    /**
     * Show custom Chooser dialog allowing to choose type of file to add.
     */
    private fun setupCustomFileTypePrompt() {
        customFileTypePrompt = MaterialDialog.Builder(context!!)
                .title("Choose a type")
                .items(R.array.chooseFileToAdd)
                .itemsIds(R.array.chooseFileToAddIds)
                .itemsCallback { _, _, position, _ ->
                    when (position) {
                        0 -> {
//                            if (externalReadWritePermissionGranted)
                                FilePickerBuilder.getInstance()
                                        .setMaxCount(30)
                                        .setActivityTheme(R.style.FilePickerTheme)
                                        .pickDocument(activity)
//                            else checkPermissions()
                        }
                        1 -> {
//                            if (cameraPermissionGranted && externalReadWritePermissionGranted)
                                customImageEntryDialog.show()
//                            else checkPermissions()
                        }
                    }
                }.build()
    }

    private fun checkPermissions() {
        requestAppPermissions(
                arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                R.string.we_need_permission_to_function, PERMISSION_REQUEST_CODE)
    }

    override fun onPermissionsGranted(requestCode: Int) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            cameraPermissionGranted = true
            externalReadWritePermissionGranted = true
        }
    }

    override fun onPermissionsDenied(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //Display message when contain some Dangerous permission not accept
        var p = ""
        for (permission in permissions) p += " ${permission.split(".").last()},"
        SnackProgressBarManager(activity!!.findViewById<View>(android.R.id.content))
                .setMessageMaxLines(4)
                .show(
                    SnackProgressBar(
                            SnackProgressBar.TYPE_NORMAL,
                            resources.getString(R.string.we_need_permission_to_function,p)
                    )
                    .setSwipeToDismiss(true)
                    .setAction("GRANT", object : SnackProgressBar.OnActionClickListener {
                        override fun onActionClick() {
                            requestPermissions(permissions,requestCode)
                        }
                    }),
                    SnackProgressBarManager.LENGTH_INDEFINITE
                )
    }

    override fun onExpand(position: Int) {
        mainListAdapter.expand(position)
    }

    override fun onCollapse(position: Int){
        mainListAdapter.collapse(position)
    }

    companion object {
        const val DOCUMENTS_LIST_POSITION = 0
        const val IMAGES_LIST_POSITION = 1
        const val SELECT_PHOTO = 1
        const val CAPTURE_PHOTO = 2

        @JvmStatic
        fun newInstance(param1: String, param2: String) = MeetingAttachmentsFragment()
    }
}