package com.flycode.timespace.data.network

import com.flycode.timespace.data.models.Response
import com.flycode.timespace.data.models.User
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AppInvitesService {

    data class Users(
        val users: ArrayList<User> = ArrayList<User>()
    )


    @POST("api/appinvites/searchForFriends")
    fun searchForFriends(
            @Body users: Users
    ): Observable<Response<ArrayList<User>>>

    @POST("api/appinvites/commitAppInvites")
    fun commitAppInvites(
            @Query("inviteBy") inviteBy: String,
            @Body users: Users
    ): Observable<ResponseBody>
}