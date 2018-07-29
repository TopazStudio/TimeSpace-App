package com.flycode.timespace.ui.main.entries.meetingEntry.meetingDetails

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.main.entries.classEntry.PlaceAutocompleteAdapter
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
open class MeetingDetailsModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            placeAutocompleteAdapter: PlaceAutocompleteAdapter,
            apolloClient: ApolloClient,
            @Named("main_tag_adapter") mainTagsAdapter: MeetingTagsAdapter,
            @Named("tag_picker_tag_adapter") tagPickerTagsAdapter: MeetingTagsAdapter
    ): MeetingDetailsPresenter = MeetingDetailsPresenter(
            placeAutocompleteAdapter = placeAutocompleteAdapter,
            apolloClient = apolloClient,
            mainTagsAdapter = mainTagsAdapter,
            tagPickerTagsAdapter = tagPickerTagsAdapter
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            MeetingDetailsFragment: MeetingDetailsFragment,
            MeetingDetailsPresenter: MeetingDetailsPresenter
    ): MeetingDetailsViewModel {
        val viewModel = ViewModelProviders.of(MeetingDetailsFragment).get(MeetingDetailsViewModel::class.java)
        MeetingDetailsPresenter.viewModel = viewModel
        viewModel.presenter = MeetingDetailsPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel2
    fun provideGeoDataClient(
            meetingDetailsFragment: MeetingDetailsFragment
    ): GeoDataClient = Places.getGeoDataClient(meetingDetailsFragment.activity!!, null)

    @Provides
    @PerFragmentLevel2
    fun providePlaceDetectionClient(
            meetingDetailsFragment: MeetingDetailsFragment
    ): PlaceDetectionClient = Places.getPlaceDetectionClient(meetingDetailsFragment.activity!!, null)


    @Provides
    @PerFragmentLevel2
    fun provideAutoCompleteAdapter(
            meetingDetailsFragment: MeetingDetailsFragment,
            geoDataClient: GeoDataClient
    )= PlaceAutocompleteAdapter(
            context = meetingDetailsFragment.context!!,
            mGeoDataClient = geoDataClient,
            mBounds = LatLngBounds(LatLng((-40).toDouble(), (-168).toDouble()), LatLng(71.0, 136.0)),
            mPlaceFilter = AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build()
    )


    @Named("main_tag_adapter")
    @Provides
    @PerFragmentLevel2
    fun provideTagAdapter(
            meetingDetailsFragment: MeetingDetailsFragment
    ): MeetingTagsAdapter = MeetingTagsAdapter(
            context = meetingDetailsFragment.context!!
    )

    @Named("tag_picker_tag_adapter")
    @Provides
    @PerFragmentLevel2
    fun provideTagPickerTagAdapter(
            meetingDetailsFragment: MeetingDetailsFragment
    ): MeetingTagsAdapter = MeetingTagsAdapter(
            context = meetingDetailsFragment.context!!
    )
}