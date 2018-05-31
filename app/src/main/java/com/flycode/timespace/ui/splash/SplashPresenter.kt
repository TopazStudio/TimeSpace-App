package com.flycode.timespace.ui.splash

import android.os.CountDownTimer
import com.flycode.timespace.ui.auth.AuthActivity
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.MainActivity

class SplashPresenter<V : SplashContract.SplashActivity>(mvpView: V)
    : BasePresenter<V>(mvpView) , SplashContract.SplashPresenter<V> {

    override fun startCounting() {
        object : CountDownTimer(WAITING_TIME, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                /**
                 * If no registered user send to authentication activity
                 * */
                if (isUserRegistered()) //Check if user registered
                    mvpView.navigateToActivity(to = MainActivity::class.java,from = null)
                else
                    mvpView.navigateToActivity(to = AuthActivity::class.java,from = null)
            }
        }.start()
    }

    /**
     * Check if the app has a registered user.
     *
     */
    private fun isUserRegistered(): Boolean {
        return utilityWrapper.defaultUser.id != 0
    }

    companion object {
        const val WAITING_TIME : Long = 1000
    }
}