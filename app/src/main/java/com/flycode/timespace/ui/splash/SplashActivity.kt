package com.flycode.timespace.ui.splash


import android.os.Bundle
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseActivity
import javax.inject.Inject

open class SplashActivity : BaseActivity() , SplashContract.SplashActivity {

    @Inject
    lateinit var presenter : SplashContract.SplashPresenter<SplashContract.SplashActivity>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.startCounting()
    }

}
