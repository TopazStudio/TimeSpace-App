package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "locations" )
data class Location (
        @PrimaryKey(autoincrement = true)
        @Column()
        var id: Int = -1,

        @Column()
        var country : String = "",
        @Column()
        var county : String = "",
        @Column()
        var city : String = "",
        @Column()
        var street : String = "",
        @Column()
        var zip_code : String = "",
        @Column()
        var landmarks : String = "",

        @Column()
        var Building : String = "",
        @Column()
        var floor : String = "",
        @Column()
        var room : String = "",

        @Column()
        var latitude : Double = 0.0,
        @Column()
        var longitude : Double = 0.0,

        @Column()
        var locatable_id : Int = 0,
        @Column()
        var locatable_type : String = ""

): BaseModel()