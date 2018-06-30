package com.flycode.timespace.ui.base

import android.arch.lifecycle.LifecycleService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.support.annotation.CallSuper
import dagger.android.AndroidInjection

open class BaseService<S : BaseServiceContract.Service, P : BaseServiceContract.Presenter<S>>
    : LifecycleService(),BaseServiceContract.Service {

    override val listeners: ArrayList<BaseServiceContract.ServiceListener> = ArrayList()

    private var mWakeLock: PowerManager.WakeLock? = null
    protected open lateinit var presenter: P

    @CallSuper
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        presenter.attachLifecycle(lifecycle)
        presenter.attachService(this as S)
    }
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        presenter.detachLifecycle(lifecycle)
        presenter.detachService()
    }

    protected fun wakeLock(get: Boolean, tag: String = "") {
        mWakeLock?.let {
            if (mWakeLock!!.isHeld) {
                mWakeLock!!.release()
            }
            mWakeLock = null
        }

        if (get) {
            val pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "TimeSpace-$tag")
            mWakeLock!!.acquire()
        }
    }
}
