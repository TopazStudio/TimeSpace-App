package com.flycode.timespace.ui.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import com.flycode.timespace.broadcastReceivers.ServiceStartedReceiver
import com.flycode.timespace.services.userService.UserService
import com.flycode.timespace.ui.base.BasePresenter

class MainPresenter
    : BasePresenter<MainActivity, MainPresenter, MainViewModel>()
        , MainContract.MainPresenter<MainActivity>{

    private lateinit var serviceStartedReceiver: ServiceStartedReceiver
    private lateinit var userServiceConnection: ServiceConnection
    private var userService : UserService? = null

    private var isBound: Boolean = false
    private var isWaitingForBind: Boolean = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun init() {
        serviceStartedReceiver = ServiceStartedReceiver().apply {
            this.listeners.add(object : ServiceStartedReceiver.OnServiceStartedListener {
                override fun onServiceStarted() {
                    syncWithUserService()
                }
            })
        }

        userServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                userService = (service as UserService.UserServiceBinder).service
                userService?.listeners?.add(view!!)
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                //                pause();
            }
        }

        startUserService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun registerUserServiceStartedBroadcast() {
        view?.registerReceiver(serviceStartedReceiver,IntentFilter(UserService.BROADCAST));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun unRegisterUserServiceStartedBroadcast() {
        view?.unregisterReceiver(serviceStartedReceiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun syncWithUserService() {
        if (UserService.isInstanceCreated)
            /*SERVICE IS RUNNING*/
            view?.bindService(
                    Intent(view, UserService::class.java),
                    userServiceConnection,
                    0
            )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun unSyncWithUserService() {
        if (isBound)
            view?.unbindService(userServiceConnection)
        userService = null
    }

    override fun startUserService(){
        if (!isBound) {
            if (view?.startService(Intent(view, UserService::class.java)) != null) { //If binding is possible
                //Wait for service connection.
            }
            //TODO: better logging
            else view?.showError("Sorry, Something went wrong. Please try again.")
        }
    }
}