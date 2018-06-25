package com.flycode.timespace.ui.appInvites

import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.appInvites.contactInvites.ContactInvitesFragment
import com.flycode.timespace.ui.appInvites.contactInvites.ContactInvitesModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(ContactInvitesModule::class)])
    abstract fun contactInvitesFragment(): ContactInvitesFragment
}