package com.flycode.timespace.ui.auth.signin

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.di.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class SignInModule {

    @Provides
    @PerFragment
    fun providePresenter(
            sharedPreferences: SharedPreferences,
            apolloClient: ApolloClient
    ): SignInPresenter
            = SignInPresenter(
            apolloClient = apolloClient,
            sharedPreferences = sharedPreferences
            )

    @Provides
    @PerFragment
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