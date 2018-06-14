package com.flycode.musclemax_app.data.models

data class Response< D > (
        var status: String,
        var message: String,
        var data : D
)