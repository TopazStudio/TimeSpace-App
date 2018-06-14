package com.flycode.timespace.data.models

import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "pictures" )
data class Picture(
        @PrimaryKey
        @Column()
        var id: Int = 0,

        @Column
        var picturable_id : Int = 0,

        @Column
        var picturable_type : String = "defaultPic",

        @Column
        var name : String = "",

        @Column
        var type : String = "",

        @Column
        var size : Int = 0,

        @Column
        var remote_location : String = "",

        @Column
        var local_location : String = "",

        @Column
        var description : String = ""
): BaseModel()