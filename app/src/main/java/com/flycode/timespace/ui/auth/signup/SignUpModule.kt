package com.flycode.timespace.ui.auth.signup

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.data.network.TempService
import com.flycode.timespace.di.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class SignUpModule {
    @Provides
    @PerFragment
    fun providePresenter(
            apolloClient: ApolloClient,
            tempService: TempService
    ): SignUpPresenter
            = SignUpPresenter(
            apolloClient = apolloClient,
            tempService = tempService
    )

    @Provides
    @PerFragment
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