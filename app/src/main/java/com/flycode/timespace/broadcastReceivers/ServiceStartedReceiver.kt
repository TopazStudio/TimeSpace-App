package com.flycode.timespace.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ServiceStartedReceiver : BroadcastReceiver() {

    val listeners: ArrayList<OnServiceStartedListener> = ArrayList()

    override fun onReceive(context: Context, intent: Intent) {
        listeners.forEach {
            it.onServiceStarted()
        }
    }

    interface OnServiceStartedListener {
        fun onServiceStarted()
    }
}
