package com.flycode.timespace.ui.main.entries.meetingEntry.meetingAttendees

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.flexible_items.SearchResultsHeaderItem
import com.flycode.timespace.ui.main.entries.meetingEntry.MeetingEntryViewModel
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter
import javax.inject.Named

@Module
open class MeetingAttendeesModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            apolloClient : ApolloClient,
            @Named("search_list_adapter") searchResultsListAdapter: FlexibleAdapter<SearchResultsHeaderItem>,
            @Named("main_list_adapter") mainListAdapter: FlexibleAdapter<PlainHeaderItem>,
            superViewModel: MeetingEntryViewModel
    ): MeetingAttendeesPresenter = MeetingAttendeesPresenter(
            searchResultsListAdapter = searchResultsListAdapter,
            mainListAdapter = mainListAdapter,
            apolloClient = apolloClient,
            superViewModel = superViewModel
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            MeetingAttendeesFragment: MeetingAttendeesFragment,
            MeetingAttendeesPresenter: MeetingAttendeesPresenter
    ): MeetingAttendeesViewModel {
        val viewModel = ViewModelProviders.of(MeetingAttendeesFragment).get(MeetingAttendeesViewModel::class.java)
        MeetingAttendeesPresenter.viewModel = viewModel
        viewModel.presenter = MeetingAttendeesPresenter
        return viewModel
    }

    @Named("main_list_adapter")
    @Provides
    @PerFragmentLevel2
    fun provideMainListAdapter(): FlexibleAdapter<PlainHeaderItem> = FlexibleAdapter(null)

    @Named("search_list_adapter")
    @Provides
    @PerFragmentLevel2
    fun provideSearchListAdapter(): FlexibleAdapter<SearchResultsHeaderItem> = FlexibleAdapter(null)
}