package com.flycode.timespace.ui.base

import android.arch.lifecycle.Lifecycle
import android.os.Bundle

interface BaseServiceContract {
    interface ServiceListener{
        fun onError(error: String)

        fun onSuccess(message: String)

        fun onFinish(success: Boolean, data: Bundle)
    }

    interface Service{

        val listeners: ArrayList<ServiceListener>

        fun onError(error: String){
            listeners.forEach {
                it.onError(error)
            }
        }

        fun onSuccess(message: String){
            listeners.forEach {
                it.onSuccess(message)
            }
        }

        fun onFinish(success: Boolean, data: Bundle){
            listeners.forEach {
                it.onFinish(success,data)
            }
        }
    }

    interface Presenter<S : Service>{
        val service: S?

        val isServiceAttached: Boolean

        fun attachLifecycle(lifecycle: Lifecycle)

        fun detachLifecycle(lifecycle: Lifecycle)

        fun attachService(service: S)

        fun detachService()

        fun onPresenterDestroy()
    }
}