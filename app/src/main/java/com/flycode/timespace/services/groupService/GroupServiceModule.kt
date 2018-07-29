package com.flycode.timespace.services.groupService

import android.content.SharedPreferences
import com.flycode.timespace.di.scope.PerService
import com.pusher.client.Pusher
import dagger.Module
import dagger.Provides

@Module
class GroupServiceModule{

    @Provides
    @PerService
    fun providePresenter(
            pusher: Pusher,
            sharedPreferences: SharedPreferences,
            groupServiceNotifications: GroupServiceNotifications
    ): GroupServicePresenter<GroupService>
            = GroupServicePresenter(
            pusher = pusher,
            sharedPreferences = sharedPreferences,
            groupServiceNotifications = groupServiceNotifications
    )

    @Provides
    @PerService
    fun provideNotifications(
            groupService: GroupService
    ): GroupServiceNotifications = GroupServiceNotifications(groupService)
}