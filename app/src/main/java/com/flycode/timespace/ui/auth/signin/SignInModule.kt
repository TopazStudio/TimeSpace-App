package com.flycode.timespace.ui.auth.signin

import dagger.Module
import dagger.Provides

@Module
class SignInModule {

    @Provides
    fun providePresenter(
            signInFragment: SignInFragment
//            apolloClient: ApolloClient
    )
            : SignInContract.SignInPresenter<SignInContract.SignInFragment>{
        return SignInPresenter(
                signInFragment
//                apolloClient
        )
    }


}