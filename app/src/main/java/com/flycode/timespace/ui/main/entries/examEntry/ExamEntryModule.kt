package com.flycode.timespace.ui.main.entries.examEntry

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
open class ExamEntryModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            placeAutocompleteAdapter: PlaceAutocompleteAdapter,
            apolloClient: ApolloClient,
            @Named("main_tag_adapter") mainTagsAdapter: ExamTagsAdapter,
            @Named("tag_picker_tag_adapter") tagPickerTagsAdapter: ExamTagsAdapter
    ): ExamEntryPresenter = ExamEntryPresenter(
            placeAutocompleteAdapter = placeAutocompleteAdapter,
            apolloClient = apolloClient,
            mainTagsAdapter = mainTagsAdapter,
            tagPickerTagsAdapter = tagPickerTagsAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            examEntryFragment: ExamEntryFragment,
            examEntryPresenter: ExamEntryPresenter
    ): ExamEntryViewModel {
        val viewModel = ViewModelProviders.of(examEntryFragment).get(ExamEntryViewModel::class.java)
        examEntryPresenter.viewModel = viewModel
        viewModel.presenter = examEntryPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideGeoDataClient(
            examEntryFragment: ExamEntryFragment
    ): GeoDataClient = Places.getGeoDataClient(examEntryFragment.activity!!, null)

    @Provides
    @PerFragmentLevel1
    fun providePlaceDetectionClient(
            examEntryFragment: ExamEntryFragment
    ): PlaceDetectionClient = Places.getPlaceDetectionClient(examEntryFragment.activity!!, null)


    @Provides
    @PerFragmentLevel1
    fun provideAutoCompleteAdapter(
            examEntryFragment: ExamEntryFragment,
            geoDataClient: GeoDataClient
    )= PlaceAutocompleteAdapter(
            context = examEntryFragment.context!!,
            mGeoDataClient = geoDataClient,
            mBounds = LatLngBounds(LatLng((-40).toDouble(), (-168).toDouble()), LatLng(71.0, 136.0)),
            mPlaceFilter = AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build()
    )

    @Named("main_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagAdapter(
            examEntryFragment: ExamEntryFragment
    ): ExamTagsAdapter = ExamTagsAdapter(
            context = examEntryFragment.context!!
    )

    @Named("tag_picker_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagPickerTagAdapter(
            examEntryFragment: ExamEntryFragment
    ): ExamTagsAdapter = ExamTagsAdapter(
            context = examEntryFragment.context!!
    )
}