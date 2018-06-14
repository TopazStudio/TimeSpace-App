package com.flycode.timespace.ui.auth

import com.flycode.timespace.di.scope.PerFragment
import com.flycode.timespace.ui.auth.landingPage.AuthLandingPageFragment
import com.flycode.timespace.ui.auth.signin.SignInFragment
import com.flycode.timespace.ui.auth.signin.SignInModule
import com.flycode.timespace.ui.auth.signup.SignUpFragment
import com.flycode.timespace.ui.auth.signup.SignUpModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragment
    @ContributesAndroidInjector()
    internal abstract fun authLandingPageFragment(): AuthLandingPageFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [(SignInModule::class)])
    internal abstract fun signInFragment(): SignInFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [(SignUpModule::class)])
    internal abstract fun signUpFragment(): SignUpFragment
}