package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

@Table(database = (Database::class), name = "timespaces" )
class TimeSpace : BaseObservable(), Serializable {
    @field: [PrimaryKey Column(name = "id")]
    var id: Int = 0

    @field: [SerializedName("time_id") Column(name = "time_id")]
    @get: Bindable
    var time_id: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("location_id") Column(name = "location_id")]
    @get: Bindable
    var location_id: String = ""
        set(value) {
            field = value
            notifyChange()
        }
}