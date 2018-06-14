package com.flycode.timespace.ui.auth.signin

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.flycode.timespace.R
import com.flycode.timespace.databinding.SignInBinding
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.sign_in_fragment.*
import javax.inject.Inject

class SignInFragment
    : BaseFragment<SignInFragment,SignInPresenter,SignInViewModel>(),
        SignInContract.SignInFragment{

    @Inject
    override lateinit var viewModel: SignInViewModel
    private lateinit var signInBinding: SignInBinding

    companion object {
        fun newInstance() = SignInFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        signInBinding = DataBindingUtil.inflate(inflater,R.layout.sign_in_fragment, container, false)
        signInBinding.viewModel = viewModel
        signInBinding.setLifecycleOwner(this)
        return signInBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sign_in_btn.setOnClickListener{
            presenter.login()
        }

        sign_up_text_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signUpFragment)
        }
    }
}
