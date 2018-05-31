package com.flycode.timespace.di.component

import android.app.Application
import com.flycode.timespace.di.module.AppModule
import com.flycode.timespace.di.module.BindingsModule
import com.flycode.timespace.di.module.DataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (BindingsModule::class),
    (AppModule::class),
    (DataModule::class)
])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}