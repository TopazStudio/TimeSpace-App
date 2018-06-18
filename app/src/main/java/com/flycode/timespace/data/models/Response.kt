package com.flycode.timespace.data.models

import com.google.gson.JsonElement

data class Response< D > (
        var message: String,
        var errors: JsonElement,
        var data : D
)