package com.flycode.timespace.ui.auth.signup

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.data.network.retrofit.TempService
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
open class SignUpModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient: ApolloClient,
            tempService: TempService,
            sharedPreferences: SharedPreferences
    ): SignUpPresenter
            = SignUpPresenter(
            apolloClient = apolloClient,
            tempService = tempService,
            sharedPreferences = sharedPreferences
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            signUpFragment: SignUpFragment,
            signUpPresenter: SignUpPresenter
    ) : SignUpViewModel{
        val viewModel = ViewModelProviders.of(signUpFragment).get(SignUpViewModel::class.java)
        signUpPresenter.viewModel = viewModel
        viewModel.presenter = signUpPresenter
        return viewModel
    }
}