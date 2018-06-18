package com.flycode.timespace.data.network

import com.flycode.timespace.data.models.LoginPayload
import com.flycode.timespace.data.models.Response
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/signInWithGoogle")
    fun signInWithGoogle(
            @Body loginPayload: LoginPayload
    ): Observable<Response<LoginPayload>>

    @POST("api/auth/signInWithFacebook")
    fun signInWithFacebook(
            @Body loginPayload: LoginPayload
    ): Observable<Response<LoginPayload>>
}