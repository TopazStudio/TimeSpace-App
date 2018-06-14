package com.flycode.timespace.ui.splash


import android.os.Bundle
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseActivity
import javax.inject.Inject

class SplashActivity
    : BaseActivity<SplashActivity,SplashPresenter,SplashViewModel>()
        , SplashContract.SplashActivity {

    @Inject
    override lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

}
