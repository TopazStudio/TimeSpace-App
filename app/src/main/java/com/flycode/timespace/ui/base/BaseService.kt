package com.flycode.timespace.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable

open class BaseService : DaggerService(), MvpService {
    protected val compositeDisposable = CompositeDisposable()
    protected val serviceEventReceiver: ServiceEventReceiver? = null
    private var mWakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun sendSuccess(message: String) {
        serviceEventReceiver?.onSuccess(message)
    }

    override fun sendError(error: String) {
        serviceEventReceiver?.onError(error)
    }

    override fun sendOnFinish(success: Boolean, data: Bundle) {
        serviceEventReceiver?.onFinish(success, data)
    }

    interface ServiceEventReceiver {
        fun onError(error: String)
        fun onSuccess(message: String)
        fun onFinish(success: Boolean, data: Bundle)
    }

    protected fun wakeLock(get: Boolean, tag: String) {
        if (mWakeLock!!.isHeld) {
            mWakeLock!!.release()
        }
        mWakeLock = null

        if (get) {
            val pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "HealthUpgrade$tag")
            mWakeLock!!.acquire()
        }
    }
}
