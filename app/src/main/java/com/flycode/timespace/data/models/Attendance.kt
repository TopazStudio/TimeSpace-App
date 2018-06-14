package com.flycode.timespace.data.models

import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "attendances" )
data class Attendance(
        @PrimaryKey(autoincrement = true)
        @Column()
        var id: Int = -1,

        @Column
        var attendable_id : Int = -1,

        @Column
        var attendable_type : String = "",

        @ForeignKey()
        var user: User? = null,

        @Column
        var confirmation : Int = 0
) : BaseModel()