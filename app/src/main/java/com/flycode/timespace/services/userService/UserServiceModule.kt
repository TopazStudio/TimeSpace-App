package com.flycode.timespace.services.userService

import android.content.SharedPreferences
import com.flycode.timespace.di.scope.PerService
import com.pusher.client.Pusher
import dagger.Module
import dagger.Provides

@Module
class UserServiceModule{

    @Provides
    @PerService
    fun providePresenter(
            pusher: Pusher,
            sharedPreferences: SharedPreferences
    ): UserServicePresenter<UserService>
            = UserServicePresenter(
            pusher = pusher,
            sharedPreferences = sharedPreferences
    )
}