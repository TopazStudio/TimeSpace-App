package com.flycode.timespace.ui.main.entries.classEntry

import android.view.View
import android.widget.AdapterView
import androidx.navigation.fragment.NavHostFragment
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.AddClazzMutation
import com.flycode.timespace.GetUserTagsQuery
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Tag
import com.flycode.timespace.data.models.apolloMappers.UserMapper
import com.flycode.timespace.type.Tagged_Input
import com.flycode.timespace.ui.base.BasePresenter
import com.google.android.gms.location.places.Places
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ClassEntryPresenter(
        val apolloClient: ApolloClient,
        val placeAutocompleteAdapter: PlaceAutocompleteAdapter,
        val mainTagsAdapter: ClassTagsAdapter,
        val tagPickerTagsAdapter: ClassTagsAdapter
) : BasePresenter<ClassEntryFragment, ClassEntryPresenter, ClassEntryViewModel>(),
        ClassEntryContract.ClassEntryPresenter<ClassEntryFragment>  {

    private fun prepareSaveClazz(): ApolloMutationCall<AddClazzMutation.Data> {
        return apolloClient.mutate(
                AddClazzMutation.builder()
                        .clazz(UserMapper.mapClazzToClazzInput(viewModel.uiState.clazz))
                        .location(UserMapper.mapLocationToLocationInput(viewModel.uiState.location))
                        .taggeds(
                                mainTagsAdapter.tagList.map {
                                        Tagged_Input
                                                .builder()
                                                .id(0.toString())
                                                .tag_id(it.id.toString())
                                                .taggable_id(viewModel.uiState.clazz.id.toString())
                                                .taggable_type("clazz")
                                                .build()
                                }
                        )
                        .time(
                                UserMapper.mapTimeToTimeInput(viewModel.uiState.time.apply {
                                    this.end_time = viewModel.endTime.timeInMillis
                                    this.start_time = viewModel.startTime.timeInMillis
                                })
                        )
                        .build()
        )
    }

    fun saveClazz(){
        view?.let {view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(prepareSaveClazz())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                if (it?.data() != null){
                                    view.showMessage("SUCCESS")
                                    NavHostFragment.findNavController(view).navigate(R.id.timeTableFragment)
                                }
                                viewModel.uiState.isLoading = false
                            },{
                                NavHostFragment.findNavController(view).navigate(R.id.timeTableFragment)
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

                viewModel.uiState.location.apply {
                    this.address = it[0].address.toString()
                    this.latLng = "${it[0].latLng.latitude},${it[0].latLng.longitude}"
                }
                it.release()
            }
        }
    }

    fun fetchTags() {
        view?.let {view ->
            viewModel.uiState.isTagsLoading = true
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
                                    tagPickerTagsAdapter.addMultipleTags(tags)
                                }
                                viewModel.uiState.isTagsLoading = false
                            },{
                                viewModel.uiState.isTagsLoading = false
                                viewModel.uiState.onTagsError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }

    fun tagClass(tag: Tag) {
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