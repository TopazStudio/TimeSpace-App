package com.flycode.timespace.ui.auth

import android.os.Bundle
import com.flycode.timespace.R
import com.flycode.timespace.databinding.AuthActivityBinding
import com.flycode.timespace.ui.base.BaseActivity
import com.flycode.timespace.util.setContentViewLazy
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    val authActivityBinding : AuthActivityBinding by setContentViewLazy(R.layout.activity_auth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authActivityBinding
    }
}
