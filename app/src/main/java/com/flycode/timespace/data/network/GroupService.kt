package com.flycode.timespace.data.network

import com.flycode.timespace.data.models.GroupMembership
import com.flycode.timespace.data.models.Response
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupService {
    @POST("api/group/joinGroup")
    fun joinGroup(
            @Body groupMembership: GroupMembership
    ): Observable<Response<GroupMembership>>

    @POST("api/group/leaveGroup")
    fun leaveGroup(
            @Body groupMembership: GroupMembership
    ): Observable<Response<GroupMembership>>
}