package com.flycode.timespace.data.models

import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "tags" )
data class Tag (
        @PrimaryKey(autoincrement = true)
        @Column()
        var id: Int = -1,

        @Column
        var name : String = "",

        @Column
        var description : String = "",

        @Column
        var color : Int = 0
): BaseModel()