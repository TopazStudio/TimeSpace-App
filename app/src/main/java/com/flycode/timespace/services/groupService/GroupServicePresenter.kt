package com.flycode.timespace.services.groupService

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.flycode.timespace.ui.base.BaseServicePresenter
import com.pusher.client.Pusher
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionStateChange
import java.lang.Exception

class GroupServicePresenter<V : GroupServiceContract.GroupService>(
        val groupServiceNotifications: GroupServiceNotifications,
        val sharedPreferences: SharedPreferences,
        val pusher: Pusher
) : BaseServicePresenter<V>(), GroupServiceContract.GroupServicePresenter<V> {

    var privateChannel: PrivateChannel? = null

    fun init(){
        service?.let {service ->

            if (privateChannel != null){
                privateChannel = null
            }

            privateChannel = pusher.subscribePrivate(
                    "private-group-${service.group.id}",
                    object : PrivateChannelEventListener{
                        override fun onEvent(p0: String?, p1: String?, p2: String?) {

                        }

                        override fun onAuthenticationFailure(p0: String?, p1: Exception?) {
                            Handler(Looper.getMainLooper()).post{
                                p1?.printStackTrace()
                                service.listeners.forEach {
                                    if (p1?.message != null){
                                        it.onError(error = p1.message.toString())
                                    }else{
                                        it.onError("Something went wrong. Please try again.")
                                    }
                                }
                            }
                        }

                        override fun onSubscriptionSucceeded(p0: String?) {

                            listenToPrivateEvent("group.new.timetable"){p0, p1, p2 ->

                            }
                        }

                    })

            pusher.connect(object : ConnectionEventListener{
                override fun onConnectionStateChange(p0: ConnectionStateChange?) {

                }

                override fun onError(p0: String?, p1: String?, p2: Exception?) {
                    Handler(Looper.getMainLooper()).post{
                        p2?.printStackTrace()
                        service.listeners.forEach {
                            if (p2?.message != null){
                                it.onError(error = p2.message.toString())
                            }else{
                                it.onError("Something went wrong. Please try again.")
                            }
                        }
                    }
                }

            })
        }

    }

    private fun listenToPrivateEvent(eventName: String,callback : (String?,String?,String?) -> Unit){
        privateChannel?.bind(eventName,object : PrivateChannelEventListener{
            override fun onEvent(p0: String?, p1: String?, p2: String?) {
                Handler(Looper.getMainLooper()).post{
                    callback(p0, p1, p2)
                }
            }

            override fun onAuthenticationFailure(p0: String?, p1: Exception?) {
                Handler(Looper.getMainLooper()).post{
                    p1?.printStackTrace()
                    service?.listeners?.forEach {
                        if (p1?.message != null){
                            it.onError(error = p1.message.toString())
                        }else{
                            it.onError("Something went wrong. Please try again.")
                        }
                    }
                }
            }

            override fun onSubscriptionSucceeded(p0: String?) {

            }

        })
    }
}