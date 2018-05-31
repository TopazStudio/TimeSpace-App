package com.flycode.timespace.ui.auth.signin

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment

import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment
    : BaseFragment(),
    SignInContract.SignInFragment{

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)

        sign_in_btn.setOnClickListener{
            //TODO: sign in logic
        }

        sign_up_text_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signUpFragment)
            //TODO: clean up
        }
    }
}
