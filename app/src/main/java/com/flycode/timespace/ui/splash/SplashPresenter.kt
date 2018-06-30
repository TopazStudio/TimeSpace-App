package com.flycode.timespace.ui.splash

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.SharedPreferences
import android.os.CountDownTimer
import com.facebook.AccessToken
import com.flycode.timespace.ui.auth.AuthActivity
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.MainActivity
import com.flycode.timespace.ui.pincode.PinCodeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn



//import com.flycode.musclemax_app.ui.main.MainActivity

class SplashPresenter(
        val sharedPreferences: SharedPreferences
) : BasePresenter<SplashActivity,SplashPresenter,SplashViewModel>()
        , SplashContract.SplashPresenter<SplashActivity> {

    val WAITING_TIME: Long = 1000

    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    override fun startCounting() {
        object : CountDownTimer(WAITING_TIME, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                /**
                 * If no registered user send to authentication activity
                 * */

                when{
                    isUserRegistered() -> view?.navigateToActivity(to = MainActivity::class.java, from = null)

                    isPinProtected() -> view?.navigateToActivity(to = PinCodeActivity::class.java, from = null)

                    else -> view?.navigateToActivity(to = AuthActivity::class.java, from = null)
                }
            }
        }.start()
    }

    /**
     * Check if the app has a registered user.
     *
     */
    private fun isUserRegistered(): Boolean {
        return utilityWrapper.defaultUser.id > 0
    }

    private fun isSignedInWithGoogle():Boolean{
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        return GoogleSignIn.getLastSignedInAccount(view!!) != null
    }

    private fun isSignedInWithFacebook() : Boolean{
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun isPinProtected(): Boolean{
        return sharedPreferences.getBoolean("pin_protected",false)
    }
}