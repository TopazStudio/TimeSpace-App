package com.flycode.timespace;

import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
import com.flycode.timespace.di.component.ApplicationComponent
import com.flycode.timespace.di.component.DaggerApplicationComponent
import com.raizlabs.android.dbflow.config.FlowManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TimeSpaceApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        //Initialize DBFlow
        FlowManager.init(this)
        Stetho.initializeWithDefaults(this)


        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    override fun applicationInjector() : AndroidInjector<DaggerApplication>? {
        val appComponent : ApplicationComponent =
                DaggerApplicationComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}