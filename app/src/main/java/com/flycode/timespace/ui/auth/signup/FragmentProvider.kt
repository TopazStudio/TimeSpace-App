package com.flycode.timespace.ui.auth.signup

import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.auth.signup.ProfilePic.ProfilePicFragment
import com.flycode.timespace.ui.auth.signup.ProfilePic.ProfilePicModule
import com.flycode.timespace.ui.auth.signup.UserDetails.UserDetailsFragment
import com.flycode.timespace.ui.auth.signup.UserDetails.UserDetailsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(ProfilePicModule::class)])
    abstract fun profilePicFragment(): ProfilePicFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(UserDetailsModule::class)])
    abstract fun bindUserDetails(): UserDetailsFragment
}