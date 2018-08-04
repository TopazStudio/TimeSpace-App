package com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails

import android.view.View
import android.widget.AdapterView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.GetUserTagsQuery
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Tag
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.entries.classEntry.PlaceAutocompleteAdapter
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryViewModel
import com.google.android.gms.location.places.Places
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetingDetailsPresenter(
        val apolloClient: ApolloClient,
        val placeAutocompleteAdapter: PlaceAutocompleteAdapter,
        val mainTagsAdapter: MeetingTagsAdapter,
        val tagPickerTagsAdapter: MeetingTagsAdapter,
        val superViewModel: MeetingEntryViewModel
) : BasePresenter<MeetingDetailsFragment, MeetingDetailsPresenter, MeetingDetailsViewModel>(),
        MeetingDetailsContract.MeetingDetailsPresenter<MeetingDetailsFragment> {

    fun onSuggestionItemPicked(adapterView: AdapterView<*>, view1: View, i: Int, l: Long) {
        view?.let { view ->
            Places.GeoDataApi.getPlaceById(
                    view.googleApiClient!!,
                    placeAutocompleteAdapter.getItem(i)?.placeId
            ).setResultCallback {
                if (!it.status.isSuccess) {
                    view.showError(view.resources.getString(R.string.something_went_wrong)!!)
                    it.release()
                }

                superViewModel.uiState.location.apply {
                    this.address = it[0].address.toString()
                    this.latLng = "${it[0].latLng.latitude},${it[0].latLng.longitude}"
                }
                it.release()
            }
        }
    }

    fun fetchTags() {
        view?.let {view ->
            viewModel.tagsEntryUiState.isTagsLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(apolloClient.query(
                            GetUserTagsQuery.builder().build()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.user()!![0]?.owned_tags()?.let {
                                    val tags = Gson().fromJson<List<Tag>>(Gson().toJson(it),
                                            object : TypeToken<List<Tag>>(){}.type
                                    )
                                    if (tags.isEmpty()){
                                        viewModel.tagsEntryUiState.isEmptyTagsHidden = false
                                    }else{
                                        viewModel.tagsEntryUiState.isEmptyTagsHidden = true
                                        tagPickerTagsAdapter.clear()
                                        tagPickerTagsAdapter.addMultipleTags(tags)
                                    }
                                }
                                viewModel.tagsEntryUiState.isTagsLoading = false
                            },{
                                viewModel.tagsEntryUiState.isTagsLoading = false
                                viewModel.tagsEntryUiState.onTagsError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }

    fun tagMeeting(tag: Tag) {
        mainTagsAdapter.addTag(tag)
        checkEmptyTags()
    }

    private fun checkEmptyTags(){
        if(mainTagsAdapter.tagList.isEmpty()){ //EMPTY
            //SHOW NO TAGS
            if (viewModel.uiState.isEmptyTagsHidden){
                viewModel.uiState.isEmptyTagsHidden = false
            }

        }else{ //NOT EMPTY
            //HIDE EMPTY TAGS
            if (!viewModel.uiState.isEmptyTagsHidden){
                viewModel.uiState.isEmptyTagsHidden = true
            }
        }
    }
}