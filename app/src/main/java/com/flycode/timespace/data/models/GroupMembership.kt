package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

@Table(database = (Database::class), name = "group_memberships" )
class GroupMembership : BaseObservable(), Serializable {

    @field: [PrimaryKey Column(name = "id")]
    var id: Int = 0

    @field: [SerializedName("user_id_2") Column(name = "user_id_2")]
    @get: Bindable
    var user_id_2 : Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("group_id_2") Column(name = "group_id_2")]
    @get: Bindable
    var group_id_2 : Int = 0
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

    @ForeignKey
    var user: User? = null

    @ForeignKey
    var group: Group? = null
}