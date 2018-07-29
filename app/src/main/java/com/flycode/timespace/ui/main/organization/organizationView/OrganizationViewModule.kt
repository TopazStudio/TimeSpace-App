package com.flycode.timespace.ui.main.organization.organizationView

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.data.network.GroupService
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import javax.inject.Named

@Module
open class OrganizationViewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient : ApolloClient,
            groupService: GroupService,
            @Named("groups_recycler_view") groupsListAdapter: FlexibleAdapter<IFlexible<*>>
    ): OrganizationViewPresenter = OrganizationViewPresenter(
            apolloClient = apolloClient,
            groupsListAdapter = groupsListAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            OrganizationViewFragment: OrganizationViewFragment,
            organizationViewPresenter: OrganizationViewPresenter
    ): OrganizationViewViewModel {
        val viewModel = ViewModelProviders.of(OrganizationViewFragment).get(OrganizationViewViewModel::class.java)
        organizationViewPresenter.viewModel = viewModel
        viewModel.presenter = organizationViewPresenter
        return viewModel
    }

    @Named("groups_recycler_view")
    @Provides
    @PerFragmentLevel1
    fun provideFlexibleListAdapter(): FlexibleAdapter<IFlexible<*>> = FlexibleAdapter(null)
}