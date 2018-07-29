package com.flycode.timespace.services.groupService

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.flycode.timespace.data.models.Group
import com.flycode.timespace.ui.base.BaseService
import javax.inject.Inject


class GroupService
    : BaseService<GroupService,GroupServicePresenter<GroupService>>(),
        GroupServiceContract.GroupService {
    @Inject
    override lateinit var presenter: GroupServicePresenter<GroupService>
    private val mBinder = GroupServiceBinder()
    override var group: Group = Group()
        set(value){
            field = value
            presenter.init()
        }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //NO SLEEP
        wakeLock(true,"GroupService")

        //ON START BROADCAST
        sendBroadcast(Intent(BROADCAST))
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock(false)

        listeners.clear()

        instance = null
    }

    /*############################### NOTIFICATIONS ################################*/


    /*############################### BINDING ################################*/

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        listeners.clear()
        return super.onUnbind(intent)
    }

    inner class GroupServiceBinder : Binder() {
        val service: GroupService
            get() = this@GroupService
    }

    companion object {
        const val BROADCAST = "com.flycode.timespace.GroupService"
        const val NEW_FOLLOWER_REQUEST_CODE = 1
        private var instance: GroupService? = null
        val isInstanceCreated : Boolean
            get() = instance != null
    }
}
