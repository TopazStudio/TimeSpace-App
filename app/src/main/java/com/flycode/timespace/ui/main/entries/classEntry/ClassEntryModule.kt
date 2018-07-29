package com.flycode.timespace.ui.main.entries.classEntry

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
open class ClassEntryModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            placeAutocompleteAdapter: PlaceAutocompleteAdapter,
            apolloClient: ApolloClient,
            @Named("main_tag_adapter") mainTagsAdapter: ClassTagsAdapter,
            @Named("tag_picker_tag_adapter") tagPickerTagsAdapter: ClassTagsAdapter
    ): ClassEntryPresenter = ClassEntryPresenter(
            placeAutocompleteAdapter = placeAutocompleteAdapter,
            apolloClient = apolloClient,
            mainTagsAdapter = mainTagsAdapter,
            tagPickerTagsAdapter = tagPickerTagsAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            ClassEntryFragment: ClassEntryFragment,
            ClassEntryPresenter: ClassEntryPresenter
    ): ClassEntryViewModel {
        val viewModel = ViewModelProviders.of(ClassEntryFragment).get(ClassEntryViewModel::class.java)
        ClassEntryPresenter.viewModel = viewModel
        viewModel.presenter = ClassEntryPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideGeoDataClient(
            classEntryFragment: ClassEntryFragment
    ): GeoDataClient = Places.getGeoDataClient(classEntryFragment.activity!!, null)

    @Provides
    @PerFragmentLevel1
    fun providePlaceDetectionClient(
            classEntryFragment: ClassEntryFragment
    ): PlaceDetectionClient = Places.getPlaceDetectionClient(classEntryFragment.activity!!, null)


    @Provides
    @PerFragmentLevel1
    fun provideAutoCompleteAdapter(
            classEntryFragment: ClassEntryFragment,
            geoDataClient: GeoDataClient
    )= PlaceAutocompleteAdapter(
            context = classEntryFragment.context!!,
            mGeoDataClient = geoDataClient,
            mBounds = LatLngBounds(LatLng((-40).toDouble(), (-168).toDouble()),LatLng(71.0, 136.0)),
            mPlaceFilter = AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build()
    )

    @Named("main_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagAdapter(
            classEntryFragment: ClassEntryFragment
    ): ClassTagsAdapter = ClassTagsAdapter(
            context = classEntryFragment.context!!
    )

    @Named("tag_picker_tag_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideTagPickerTagAdapter(
            classEntryFragment: ClassEntryFragment
    ): ClassTagsAdapter = ClassTagsAdapter(
            context = classEntryFragment.context!!
    )
}