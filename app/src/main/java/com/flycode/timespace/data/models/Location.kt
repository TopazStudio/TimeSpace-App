package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

@Table(database = (Database::class), name = "locations" )
class Location: BaseObservable(), Serializable {
    @PrimaryKey(autoincrement = true)
    @Column()
    var id: Int = -1

    @field: [SerializedName("address") Column(name = "address")]
    @get: Bindable
    var address: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("building") Column(name = "building")]
    @get: Bindable
    var building: String = ""
        set(value) {
            field = value
            notifyChange()
        }
    @field: [SerializedName("floor") Column(name = "floor")]
    @get: Bindable
    var floor: String = ""
        set(value) {
            field = value
            notifyChange()
        }
    @field: [SerializedName("room") Column(name = "room")]
    @get: Bindable
    var room: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("latLng") Column(name = "latLng")]
    @get: Bindable
    var latLng: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @Column()
    var locatable_id: Int = 0
    @Column()
    var locatable_type: String = ""

}