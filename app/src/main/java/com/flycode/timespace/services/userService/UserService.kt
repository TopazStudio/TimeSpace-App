package com.flycode.timespace.services.userService

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.BADGE_ICON_SMALL
import android.support.v4.app.NotificationCompat.VISIBILITY_PRIVATE
import android.widget.RemoteViews
import com.flycode.timespace.R
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BaseService
import com.flycode.timespace.ui.main.user.userView.UserViewFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject


class UserService
    : BaseService<UserService,UserServicePresenter<UserService>>(),
        UserServiceContract.UserService {
    @Inject
    override lateinit var presenter: UserServicePresenter<UserService>
    private var notificationManager: NotificationManager? = null
    private var remoteViews: RemoteViews? = null
    private val mBinder = UserServiceBinder()

    override fun onCreate() {
        super.onCreate()
        instance = this

        //NOTIFICATION MANAGER
        notificationManager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)

        //NO SLEEP
        wakeLock(true,"UserService")

        //ON START BROADCAST
        sendBroadcast(Intent(BROADCAST))
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock(false)

        //REMOVE ALL NOTIFICATIONS
        notificationManager?.cancel(R.string.new_follower_notification_id)

        listeners.clear()
        instance = null
    }

    /*############################### NOTIFICATIONS ################################*/

    override fun showNewFollowerNotification(data: String) {
        val userFollower = Gson().fromJson(
                Gson().fromJson(data,JsonObject::class.java)
                        .getAsJsonObject("user"),
                User::class.java
        )
        //TODO: user deep link
        val contentIntent = PendingIntent.getActivity(
                this,
                NEW_FOLLOWER_REQUEST_CODE,
                Intent(this, UserViewFragment::class.java).apply {
                    this.putExtra("userFollower",data)
                },
                0
        )
        var followerCount = 1
        remoteViews = RemoteViews(packageName, R.layout.new_follower_notification).apply {
            this.setTextViewText(R.id.followers_count,followerCount.toString())
            this.setTextViewText(R.id.new_followers_text,getString(R.string.new_follower))
            this.setTextViewText(R.id.first_follower_name,"${userFollower.name_prefix} ${userFollower.first_name} ${userFollower.second_name} ${userFollower.surname}")
            this.setTextViewText(R.id.other_followers,"")
//            notificationManager.notify(R.string.step_counter_notification_id, notification);
        }

        // Set the info for the views that show in the notification panel.
        val notification = NotificationCompat.Builder(this, getString(R.string.new_follower_notification_channel))
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setTicker("${getString(R.string.you_have)} $followerCount ${getString(R.string.new_follower)}")
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.ic_timespace_small_logo)
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))
                .setBadgeIconType(BADGE_ICON_SMALL)
                .setAutoCancel(true)
                .setVisibility(VISIBILITY_PRIVATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(Uri.parse("android.resource://$packageName/raw/appointed"))
                .build()
//
//        startForeground(R.string.step_counter_notification_id, notification)

        // Send the notification.
        notificationManager?.notify(R.string.new_follower_notification_id, notification)
    }

    /*############################### BINDING ################################*/

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        listeners.clear()
        return super.onUnbind(intent)
    }

    inner class UserServiceBinder : Binder() {
        val service: UserService
            get() = this@UserService
    }

    companion object {
        const val BROADCAST = "com.flycode.timespace.UserService"
        const val NEW_FOLLOWER_REQUEST_CODE = 1
        private var instance: UserService? = null
        val isInstanceCreated : Boolean
            get() = instance != null
    }
}
