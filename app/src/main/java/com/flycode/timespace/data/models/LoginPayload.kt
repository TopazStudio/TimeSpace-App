package com.flycode.timespace.data.models

import com.google.gson.annotations.SerializedName

data class LoginPayload(
        @field: SerializedName("token")
        var token : String,

        @field: SerializedName("user")
        var user : User
)