package com.flycode.timespace.ui.main.groups.groupsOverview

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.data.network.GroupService
import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import javax.inject.Named

@Module
open class GroupsOverviewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient : ApolloClient,
            groupService: GroupService,
            @Named("search_list_adapter") searchResultsListAdapter: FlexibleAdapter<ISectionable<*,*>>,
            @Named("main_list_adapter") mainListAdapter: FlexibleAdapter<ExpandableHeaderItem>
    ): GroupsOverviewPresenter
            = GroupsOverviewPresenter(
            apolloClient = apolloClient,
            searchResultsListAdapter = searchResultsListAdapter,
            mainListAdapter = mainListAdapter,
            groupService = groupService
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            groupsOverviewFragment: GroupsOverviewFragment,
            groupsOverviewPresenter: GroupsOverviewPresenter
    ) : GroupsOverviewViewModel {
        val viewModel = ViewModelProviders.of(groupsOverviewFragment).get(GroupsOverviewViewModel::class.java)
        groupsOverviewPresenter.viewModel = viewModel
        viewModel.presenter = groupsOverviewPresenter
        return viewModel
    }

    @Named("search_list_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideSearchListAdapter(): FlexibleAdapter<ISectionable<*, *>>  = FlexibleAdapter(null)

    @Named("main_list_adapter")
    @Provides
    @PerFragmentLevel1
    fun provideMainListAdapter(): FlexibleAdapter<ExpandableHeaderItem>  = FlexibleAdapter(null)
}