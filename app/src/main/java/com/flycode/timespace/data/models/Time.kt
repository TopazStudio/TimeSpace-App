package com.flycode.timespace.data.models

import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "times" )
data class Time(
        @PrimaryKey(autoincrement = true)
        @Column()
        var id: Int = -1,

        @Column
        var timable_id : Int = -1,

        @Column
        var timable_type : String = "",

        @Column
        var weekly_repeats: Int = 0,

        @Column
        var start_time : Long = 0,

        @Column
        var end_time : Long = 0
): BaseModel()