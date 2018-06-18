package com.flycode.timespace.ui.auth.signin

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.data.network.AuthService
import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.Module
import dagger.Provides

@Module
class SignInModule {

    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            sharedPreferences: SharedPreferences,
            apolloClient: ApolloClient,
            authService: AuthService,
            googleSignInClient: GoogleSignInClient
    ): SignInPresenter
            = SignInPresenter(
            authService = authService,
            googleSignInClient = googleSignInClient,
            apolloClient = apolloClient,
            sharedPreferences = sharedPreferences
            )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            signInFragment: SignInFragment,
            signInPresenter: SignInPresenter
    ) : SignInViewModel{
        val viewModel = ViewModelProviders.of(signInFragment).get(SignInViewModel::class.java)
        signInPresenter.viewModel = viewModel
        viewModel.presenter = signInPresenter
        return viewModel
    }

}