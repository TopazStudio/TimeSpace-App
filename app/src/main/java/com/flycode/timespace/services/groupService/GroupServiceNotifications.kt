package com.flycode.timespace.services.groupService

import android.app.NotificationManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleService
import android.arch.lifecycle.OnLifecycleEvent
import android.widget.RemoteViews
import com.flycode.timespace.R

class GroupServiceNotifications(
        val groupService: GroupService
) {
    private var remoteViews: RemoteViews? = null
    private var notificationManager: NotificationManager? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init(){
        //NOTIFICATION MANAGER
        notificationManager = (groupService.getSystemService(LifecycleService.NOTIFICATION_SERVICE) as NotificationManager)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy(){
        //REMOVE ALL NOTIFICATIONS
        notificationManager?.cancel(R.string.new_follower_notification_id)
    }
}