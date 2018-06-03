package com.flycode.timespace.ui.auth.signin

import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.base.MvpView

class SignInPresenter<V : MvpView>(
        val mvpFragment: V
//        val apolloClient: ApolloClient
)
    : BasePresenter<V>(mvpFragment),
    SignInContract.SignInPresenter<V>{

    override fun login(email: String, password: String){
        /*apolloClient
                .mutate(
                        .builder()
                                .limit(10)
                                .type(FeedType.HOT)
                                .build()
                )
                .httpCachePolicy(HttpCachePolicy.CACHE_FIRST)*/

    }


}