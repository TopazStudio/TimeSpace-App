package com.flycode.timespace.di.module

import com.flycode.timespace.di.scope.PerActivity
import com.flycode.timespace.di.scope.PerService
import com.flycode.timespace.services.userService.UserService
import com.flycode.timespace.services.userService.UserServiceModule
import com.flycode.timespace.ui.appInvites.AppInvitesActivity
import com.flycode.timespace.ui.appInvites.AppInvitesModule
import com.flycode.timespace.ui.appInvites.FragmentProvider
import com.flycode.timespace.ui.auth.AuthActivity
import com.flycode.timespace.ui.main.MainActivity
import com.flycode.timespace.ui.main.MainModule
import com.flycode.timespace.ui.organization.organizationView.OrganizationViewActivity
import com.flycode.timespace.ui.organization.organizationView.OrganizationViewModule
import com.flycode.timespace.ui.splash.SplashActivity
import com.flycode.timespace.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingsModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [(SplashModule::class)])
    abstract fun splashActivity(): SplashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(com.flycode.timespace.ui.auth.FragmentProvider::class)])
    abstract fun authActivity(): AuthActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainModule::class),(com.flycode.timespace.ui.main.FragmentProvider::class)])
    abstract fun mainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(OrganizationViewModule::class)])
    abstract fun organizationViewActivity(): OrganizationViewActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(AppInvitesModule::class),(FragmentProvider::class)])
    abstract fun appInvitesFragment(): AppInvitesActivity

    @PerService
    @ContributesAndroidInjector(modules = [(UserServiceModule::class)])
    abstract fun userService(): UserService
}
