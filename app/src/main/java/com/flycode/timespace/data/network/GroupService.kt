package com.flycode.timespace.data.network

import com.flycode.timespace.data.models.GroupMember
import com.flycode.timespace.data.models.Response
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupService {
    @POST("api/group/joinGroup")
    fun joinGroup(
            @Body groupMember: GroupMember
    ): Observable<Response<GroupMember>>

    @POST("api/group/leaveGroup")
    fun leaveGroup(
            @Body groupMember: GroupMember
    ): Observable<Response<GroupMember>>
}