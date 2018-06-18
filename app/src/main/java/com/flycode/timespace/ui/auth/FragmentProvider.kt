package com.flycode.timespace.ui.auth

import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.ui.auth.landingPage.AuthLandingPageFragment
import com.flycode.timespace.ui.auth.signin.SignInFragment
import com.flycode.timespace.ui.auth.signin.SignInModule
import com.flycode.timespace.ui.auth.signup.SignUpFragment
import com.flycode.timespace.ui.auth.signup.SignUpModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragmentLevel1
    @ContributesAndroidInjector()
    abstract fun authLandingPageFragment(): AuthLandingPageFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(SignInModule::class)])
    abstract fun signInFragment(): SignInFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(SignUpModule::class),(com.flycode.timespace.ui.auth.signup.FragmentProvider::class)])
    abstract fun signUpFragment(): SignUpFragment
}