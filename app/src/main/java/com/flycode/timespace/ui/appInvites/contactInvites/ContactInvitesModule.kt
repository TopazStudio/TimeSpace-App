package com.flycode.timespace.ui.appInvites.contactInvites

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
class ContactInvitesModule{
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            mainListAdapter : FlexibleAdapter<ExpandableHeaderItem>
    ): ContactInvitesPresenter = ContactInvitesPresenter(
            mainListAdapter = mainListAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            appInvitesActivity: ContactInvitesFragment,
            appInvitesPresenter: ContactInvitesPresenter
    ) : ContactInvitesViewModel {
        val viewModel = ViewModelProviders.of(appInvitesActivity).get(ContactInvitesViewModel::class.java)
        appInvitesPresenter.viewModel = viewModel
        viewModel.presenter = appInvitesPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideMainListAdapter(): FlexibleAdapter<ExpandableHeaderItem> = FlexibleAdapter(null)
}