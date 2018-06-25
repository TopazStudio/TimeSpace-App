package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

@Table(database = (Database::class), name = "group_members" )
class GroupMember : BaseObservable(), Serializable {

    @field: [PrimaryKey Column(name = "id")]
    var id: Int = 0

    @field: [SerializedName("user_id") Column(name = "user_id")]
    @get: Bindable
    var user_id : Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("group_id") Column(name = "group_id")]
    @get: Bindable
    var group_id : Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("join_status") Column(name = "join_status")]
    @get: Bindable
    var join_status : String = ""
        set(value) {
            field = value
            notifyChange()
        }
}