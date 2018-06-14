package com.flycode.timespace.ui.auth.signin


import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.Login
import com.flycode.timespace.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignInPresenter(
        val apolloClient: ApolloClient,
        val sharedPreferences: SharedPreferences
) : BasePresenter<SignInFragment,SignInPresenter,SignInViewModel>(),
        SignInContract.SignInPresenter<SignInFragment>{

    override fun login(){
        view?.let { view ->
            compositeDisposable.add(
                    Rx2Apollo.from(loginUser())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it?.data()?.let {it1 ->
                                    sharedPreferences.edit().putString("token",it1.login()?.token()).apply()
                                    view.showMessage("Logged in successfully.")
                                }
                            },{
                                view.hideLoading()
                                view.showError(it.message!!)
                            })
            )
        }
    }

    private fun loginUser(): ApolloMutationCall<Login.Data> {
        return apolloClient.mutate(
                Login.builder()
                        .email(viewModel.credentials.email)
                        .password(viewModel.credentials.password)
                        .build()
        )
    }

}