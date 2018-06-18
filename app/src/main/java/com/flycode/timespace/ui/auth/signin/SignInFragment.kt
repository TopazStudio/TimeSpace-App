package com.flycode.timespace.ui.auth.signin

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.flycode.timespace.R
import com.flycode.timespace.databinding.SignInBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.sign_in_fragment.*
import java.util.*
import javax.inject.Inject

class SignInFragment
    : BaseFragment<SignInFragment,SignInPresenter,SignInViewModel>(),
        SignInContract.SignInFragment{

    @Inject
    override lateinit var viewModel: SignInViewModel
    private lateinit var signInBinding: SignInBinding
    private val SIGN_IN_BY_GOOGLE_RESPONSE_CODE = 1
    private val callbackManager : CallbackManager = CallbackManager.Factory.create()

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

        btn_google.setOnClickListener{
            presenter.signInWithGoogle()
        }

        LoginManager.getInstance().registerCallback(callbackManager, presenter.signInWithFacebookCallback())
        btn_facebook.setOnClickListener{
            showLoading()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                    "public_profile","email"
            ))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_BY_GOOGLE_RESPONSE_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            presenter.handleSignInWithGoogleResult(task)
        }
    }
}
