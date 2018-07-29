package com.flycode.timespace.ui.main.groups.groupsView

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import com.flycode.timespace.broadcastReceivers.ServiceStartedReceiver
import com.flycode.timespace.services.groupService.GroupService
import com.flycode.timespace.ui.base.BasePresenter

class GroupViewPresenter
    : BasePresenter<GroupViewFragment, GroupViewPresenter, GroupViewViewModel>(),
        GroupViewContract.GroupViewPresenter<GroupViewFragment> {
    private lateinit var serviceStartedReceiver: ServiceStartedReceiver
    private lateinit var groupServiceConnection: ServiceConnection
    private var groupService : GroupService? = null

    private var isBound: Boolean = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun init() {
        serviceStartedReceiver = ServiceStartedReceiver().apply {
            this.listeners.add(object : ServiceStartedReceiver.OnServiceStartedListener {
                override fun onServiceStarted() {
                    syncWithGroupService()
                }
            })
        }

        groupServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                groupService = (service as GroupService.GroupServiceBinder).service
                groupService?.listeners?.add(view!!)
                groupService?.group = viewModel.uiState.group
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                //                pause();
            }
        }

        startGroupService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun registerGroupServiceStartedBroadcast() {
        view?.activity?.registerReceiver(serviceStartedReceiver, IntentFilter(GroupService.BROADCAST));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun unRegisterGroupServiceStartedBroadcast() {
        view?.activity?.unregisterReceiver(serviceStartedReceiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun syncWithGroupService() {
        if (GroupService.isInstanceCreated)
        /*SERVICE IS RUNNING*/
            view?.activity?.bindService(
                    Intent(view?.context, GroupService::class.java),
                    groupServiceConnection,
                    0
            )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun unSyncWithGroupService() {
        if (isBound)
            view?.activity?.unbindService(groupServiceConnection)
        groupService = null
    }

    override fun startGroupService(){
        if (!GroupService.isInstanceCreated && !isBound) {
            if (view?.activity?.startService(Intent(view?.context, GroupService::class.java)) != null) {
                //If binding is possible
                //Wait for service connection.
            }
            //TODO: better logging
            else view?.showError("Sorry, Something went wrong. Please try again.")
        }
    }
}