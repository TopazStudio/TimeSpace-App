package com.flycode.timespace.ui.auth.landingPage


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment

import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_auth_landing_page.*

class AuthLandingPageFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_landing_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sign_in_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signInFragment)
        }

        sign_up_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signUpFragment)
        }
    }

}
