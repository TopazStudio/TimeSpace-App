package com.flycode.timespace.ui.appInvites.contactInvites

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.data.network.AppInvitesService
import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.appInvites.AppInvitesViewModel
import com.flycode.timespace.ui.flexible_items.ContactsListHeaderItem
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
class ContactInvitesModule{
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            mainListAdapter : FlexibleAdapter<ContactsListHeaderItem>,
            appInvitesService: AppInvitesService,
            superViewModel: AppInvitesViewModel
    ): ContactInvitesPresenter = ContactInvitesPresenter(
            mainListAdapter = mainListAdapter,
            appInvitesService = appInvitesService,
            superViewModel = superViewModel
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
    fun provideMainListAdapter(): FlexibleAdapter<ContactsListHeaderItem> = FlexibleAdapter(null)
}