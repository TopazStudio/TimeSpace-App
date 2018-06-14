package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable

class Credentials: BaseObservable(){
    @get: Bindable
    var email: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @get: Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyChange()
        }
}

