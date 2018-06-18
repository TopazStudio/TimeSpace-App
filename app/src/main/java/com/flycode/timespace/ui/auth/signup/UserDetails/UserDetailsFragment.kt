package com.flycode.timespace.ui.auth.signup.UserDetails

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.NavHostFragment
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.flycode.timespace.R
import com.flycode.timespace.databinding.UserDetailsBinding
import com.flycode.timespace.ui.auth.signup.SignUpFragment
import com.flycode.timespace.ui.auth.signup.SignUpViewModel
import com.flycode.timespace.ui.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.fragment_user_details.*
import java.util.*
import javax.inject.Inject





class UserDetailsFragment
    : BaseFragment<UserDetailsFragment, UserDetailsPresenter, UserDetailsViewModel>(),
        UserDetailsContract.UserDetailsFragment{

    @Inject
    override lateinit var viewModel: UserDetailsViewModel
    @Inject
    lateinit var superViewModel: SignUpViewModel
    @Inject
    lateinit var signUpFragment: SignUpFragment

    private lateinit var signUpBinding: UserDetailsBinding
    private val callbackManager : CallbackManager = CallbackManager.Factory.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        signUpBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_details, container, false)
        signUpBinding.superViewModel = superViewModel
        signUpBinding.setLifecycleOwner(this)
        return signUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        btn_next.setOnClickListener{
            if(checkPasswordRetype()) //TODO: validate data.
                NavHostFragment.findNavController(this).navigate(R.id.profilePicFragment)
        }
        setupNamePrefix()
    }

    private fun setupNamePrefix() {
        val adapter = ArrayAdapter.createFromResource(activity,
                R.array.name_prefix, R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        name_prefix_spinner.adapter = adapter

        name_prefix_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                superViewModel.user.name_prefix = resources.getStringArray(R.array.name_prefix)[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sign_in_text_btn.setOnClickListener{
            signUpFragment.onSignIn()
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

    private fun checkPasswordRetype(): Boolean{
        return if(superViewModel.uiState.facebookLoginSuccess || superViewModel.uiState.googleLoginSuccess){ //if authenticated externally, no need for password
            true
        } else if (et_retype_password.text.toString() != et_password.text.toString()){
            showError("The passwords do not match")
            false
        }else true
    }

    companion object {
        const val SIGN_IN_BY_GOOGLE_RESPONSE_CODE = 1

        fun newInstance() : UserDetailsFragment{
            return UserDetailsFragment()
        }
    }
}
