package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttachments

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.graphics.Bitmap
import com.flycode.timespace.data.models.Document
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.DocumentSectionableListItem
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

class MeetingAttachmentsPresenter(
        val mainListAdapter: FlexibleAdapter<PlainHeaderItem>
) : BasePresenter<MeetingAttachmentsFragment, MeetingAttachmentsPresenter, MeetingAttachmentsViewModel>(),
        MeetingAttachmentsContract.MeetingAttachmentsPresenter<MeetingAttachmentsFragment> {

    /**
     * Get selected files as paths
     * */
    fun addFilePaths(filePaths: ArrayList<String>) {
        filePaths.forEach {
            val file = File(it)
            addItemToMainList(
                    MeetingAttachmentsFragment.DOCUMENTS_LIST_POSITION,
                    DocumentSectionableListItem(
                            viewModel.headingsList[MeetingAttachmentsFragment.DOCUMENTS_LIST_POSITION],
                            Document(name = file.name,
                                    type = file.extension,
                                    size = file.length(),
                                    local_location = it),
                            view?.context
                    )
            )
        }
    }

    private fun addItemToMainList(headerPosition: Int , documentSectionableListItem: DocumentSectionableListItem){
        //Search in the main joined list
        if (viewModel.headingsList[headerPosition].subItems != null) { //Something is there
            var found = false
            viewModel.headingsList[headerPosition].subItems.forEach {
                if ((it as DocumentSectionableListItem).document.name == documentSectionableListItem.document.name){ //if it is in the list
                    found = true
                }
            }
            if (!found){ //If not found add the item
                viewModel.headingsList[headerPosition].addSubItem(documentSectionableListItem)
                viewModel.headingsList[headerPosition].entries = 1 + viewModel.headingsList[headerPosition].entries
                mainListAdapter.notifyDataSetChanged()
                checkEmptyAssignments()
            }
        } else { //Nothing in the subItems. Add the item.
            viewModel.headingsList[headerPosition].addSubItem(documentSectionableListItem)
            viewModel.headingsList[headerPosition].entries = 1 + viewModel.headingsList[headerPosition].entries
            mainListAdapter.notifyDataSetChanged()
            checkEmptyAssignments()
        }
    }

    /**
     * Get captured image as bitmap.
     * */
    fun onCaptureImageResult(data: Intent) {
        view?.let { view ->
            viewModel.uiState.isMainLoading = true
            compositeDisposable.add(
                    Observable.create<Bitmap> { emitter ->
                        val imageBitmap = data.extras.get("data") as Bitmap
                        emitter.onNext(imageBitmap)
                        emitter.onComplete()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        addItemToMainList(
                                MeetingAttachmentsFragment.IMAGES_LIST_POSITION,
                                DocumentSectionableListItem(
                                        viewModel.headingsList[MeetingAttachmentsFragment.IMAGES_LIST_POSITION],
                                        Document(
                                                name = "image_${System.currentTimeMillis()}.jpg",
                                                type = "jpg",
                                                size = it.allocationByteCount.toLong()
                                        ),
                                        view.context,
                                        it
                                )
                        )
                        viewModel.uiState.isMainLoading = false
                    },{ throwable ->
                        viewModel.uiState.isMainLoading = false
                        viewModel.uiState.onMainError = true
                        view.showError(throwable.message!!)
                    })
            )
        }
    }

    /**
     * Gets picked images as paths to avoid unnecessary ui lagging.
     * */
    fun onPickerImageResult(data: Intent) {
        if(data.clipData != null) {
            val count = data.clipData.itemCount
            for(i in 1 until count) {
                val file = File(data.clipData.getItemAt(i).uri.path)
                addItemToMainList(
                        MeetingAttachmentsFragment.IMAGES_LIST_POSITION,
                        DocumentSectionableListItem(
                                viewModel.headingsList[MeetingAttachmentsFragment.IMAGES_LIST_POSITION],
                                Document(name = file.name,
                                        type = file.extension,
                                        size = file.length(),
                                        local_location = data.clipData.getItemAt(i).uri.path),
                                view?.context
                        )
                )
            }

        } else if(data.data != null) {
            val file = File(data.data.path)
            addItemToMainList(
                    MeetingAttachmentsFragment.IMAGES_LIST_POSITION,
                    DocumentSectionableListItem(
                            viewModel.headingsList[MeetingAttachmentsFragment.IMAGES_LIST_POSITION],
                            Document(
                                    name = file.name,
                                    type = file.extension,
                                    size = file.length(),
                                    local_location = data.data.path),
                            view?.context
                    )
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        checkEmptyAssignments()
    }

    private fun checkEmptyAssignments(){
        if(viewModel.headingsList.all { it.subItems == null || it.subItems.isEmpty() }){ //EMPTY
            //SHOW EMPTY ASSIGNMENTS HINT
            if (!viewModel.uiState.showEmptyAttachmentsHint){
                viewModel.uiState.showEmptyAttachmentsHint = true
            }

        }else{ //NOT EMPTY
            //HIDE EMPTY ASSIGNMENTS HINT
            if (viewModel.uiState.showEmptyAttachmentsHint){
                viewModel.uiState.showEmptyAttachmentsHint = false
            }
        }
    }
}