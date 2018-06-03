package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = (Database::class), name = "examinations" )
data class Examination(
        @field: [PrimaryKey(autoincrement = true) Column()]
        var id: Int = -1,

        @field: Column()
        var owner_id: Int = 0,

        @field: Column()
        var time_table_id: Int = 0,

        @field: Column()
        var name: String = "",

        @field: Column()
        var note: String = "",

        @field: Column()
        var color: Int = 0,

        @field: Column()
        var description: String = ""
): BaseObservable()