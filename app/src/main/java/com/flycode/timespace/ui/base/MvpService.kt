package com.flycode.timespace.ui.base

import android.os.Bundle

interface MvpService {

    fun sendSuccess(message: String)

    fun sendError(error: String)

    fun sendOnFinish(success: Boolean, data: Bundle)

    fun stopSelf()

}
