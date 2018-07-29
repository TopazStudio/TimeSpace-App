package com.flycode.timespace.ui.main.entries.meetingEntry

import com.flycode.timespace.ui.base.BasePresenter

class MeetingEntryPresenter
    : BasePresenter<MeetingEntryFragment, MeetingEntryPresenter, MeetingEntryViewModel>(),
        MeetingEntryContract.MeetingEntryPresenter<MeetingEntryFragment> {

//    fun prepareSaveMeeting(){
//
//    }
//
//    fun saveMeeting(){
//        view?.let {view ->
//            viewModel.uiState.isLoading = true
//            compositeDisposable.add(
//                    Rx2Apollo.from(prepareSaveMeeting())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe ({
//                                if (it?.data() != null){
//                                    view.showMessage("SUCCESS")
//                                    NavHostFragment.findNavController(view).navigate(R.id.timeTableFragment)
//                                }
//                                viewModel.uiState.isLoading = false
//                            },{
//                                viewModel.uiState.isLoading = false
//                                viewModel.uiState.onError = true
//                                if (it.message != null){
//                                    view.showError(it.message.toString())
//                                }else{
//                                    view.showError(view.resources.getString(R.string.something_went_wrong))
//                                }
//                            })
//            )
//        }
//    }
}