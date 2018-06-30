package com.flycode.timespace.services.userService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
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

class UserServicePresenter<V : UserServiceContract.UserService>(
        val sharedPreferences: SharedPreferences,
        val pusher: Pusher
) : BaseServicePresenter<V>(), UserServiceContract.UserServicePresenter<V> {
    lateinit var privateChannel: PrivateChannel

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init(){
        val user = utilityWrapper.defaultUser
        privateChannel = pusher.subscribePrivate(
                "private-user-${user.id}",
                object : PrivateChannelEventListener{
                        override fun onEvent(p0: String?, p1: String?, p2: String?) {

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
                            listenToFollowersEvents()
                        }

                })

        pusher.connect(object : ConnectionEventListener{
            override fun onConnectionStateChange(p0: ConnectionStateChange?) {

            }

            override fun onError(p0: String?, p1: String?, p2: Exception?) {

            }

        })
    }

    private fun listenToFollowersEvents(){
        privateChannel.bind("user.following",object : PrivateChannelEventListener{
            override fun onEvent(p0: String?, p1: String?, p2: String?) {
                Handler(Looper.getMainLooper()).post{
                    service?.showNewFollowerNotification(p2!!)
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