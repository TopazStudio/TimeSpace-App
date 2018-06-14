package com.flycode.timespace.ui.auth

import android.os.Bundle
import com.flycode.timespace.R
import com.flycode.timespace.util.setContentViewLazy
import dagger.android.support.DaggerAppCompatActivity

class AuthActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}
