package com.flycode.timespace.ui.auth.signup.UserDetails

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.flycode.timespace.data.network.AuthService
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.flycode.timespace.ui.auth.signup.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.Module
import dagger.Provides

@Module
open class UserDetailsModule {

    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            authService: AuthService,
            superViewModel: SignUpViewModel,
            googleSignInClient: GoogleSignInClient,
            sharedPreferences: SharedPreferences
    ): UserDetailsPresenter
            = UserDetailsPresenter(
            authService = authService,
            superViewModel = superViewModel,
            googleSignInClient = googleSignInClient,
            sharedPreferences = sharedPreferences
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            userDetailsFragment: UserDetailsFragment,
            userDetailsPresenter: UserDetailsPresenter
    ) : UserDetailsViewModel{
        val viewModel = ViewModelProviders.of(userDetailsFragment).get(UserDetailsViewModel::class.java)
        userDetailsPresenter.viewModel = viewModel
        viewModel.presenter = userDetailsPresenter
        return viewModel
    }
}