package com.flycode.timespace.ui.main.entries.meetingEntry

import androidx.navigation.fragment.NavHostFragment
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.AddMeetingMutation
import com.flycode.timespace.R
import com.flycode.timespace.data.models.TimeTable
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.models.apolloMappers.UserMapper
import com.flycode.timespace.type.Tagged_Input
import com.flycode.timespace.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingEntryPresenter(
        val apolloClient: ApolloClient
) : BasePresenter<MeetingEntryFragment, MeetingEntryPresenter, MeetingEntryViewModel>(),
        MeetingEntryContract.MeetingEntryPresenter<MeetingEntryFragment> {

    private fun prepareSaveMeeting(): ApolloMutationCall<AddMeetingMutation.Data> {
        return apolloClient.mutate(
                AddMeetingMutation.builder()
                        .meeting(UserMapper.mapMeetingToMeetingInput(viewModel.uiState.meeting.apply {
                            //                            this.owner = utilityWrapper.defaultUser
                            this.owner = User().apply { this.id = 1 }
                            this.timeTable = TimeTable(id = 1)
                        }))
                        .location(UserMapper.mapLocationToLocationInput(viewModel.uiState.location))
                        .taggeds(
                                viewModel.tagList.map {
                                    Tagged_Input
                                            .builder()
                                            .id(0.toString())
                                            .tag_id(it.id.toString())
                                            .taggable_id(viewModel.uiState.meeting.id.toString())
                                            .taggable_type("meeting")
                                            .build()
                                }
                        )
                        .time(
                                UserMapper.mapTimeToTimeInput(viewModel.uiState.time.apply {
                                    this.end_time = viewModel.endTime.timeInMillis
                                    this.start_time = viewModel.startTime.timeInMillis
                                })
                        )
                        .documents(
                                viewModel.documents.map {
                                    UserMapper.mapDocumentToDocumentInput(it)
                                }
                        )
                        .users(
                                viewModel.attendees.map {
                                    UserMapper.mapUserToUserInput(it)
                                }
                        )
                        .build()
        )
    }

    fun saveMeeting(){
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(prepareSaveMeeting())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                if (it?.data() != null){
                                    view.showMessage("SUCCESS")
                                    NavHostFragment.findNavController(view).navigate(R.id.timeTableFragment)
                                }
                                viewModel.uiState.isLoading = false
                            },{
                                viewModel.uiState.isLoading = false
                                viewModel.uiState.onError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }
}