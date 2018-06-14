package com.flycode.timespace.data.network

import com.flycode.musclemax_app.data.models.Response
import com.flycode.timespace.data.models.Picture
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface TempService {
    @Multipart
    @POST("api/temp/storeTempPic")
    fun tempSaveImage(
            @Part image: MultipartBody.Part
    ): Observable<Response<Picture>>
}