package com.flycode.timespace.data.models

import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "timetables" )
@ManyToMany(referencedTable = Tag::class)
data class TimeTable(
        @PrimaryKey(autoincrement = true)
        @Column()
        var id: Int = -1,

        @field: Column()
        var name: String = "",

        @field: Column()
        var type: String = "",

        @field: Column()
        var status: Int = 0,

        @field: Column()
        var color: Int = 0,

        @field: Column()
        var priority: Int = 0,

        @ForeignKey()
        var owner: User? = null,

        @ForeignKey()
        var group: Group? = null

        //The one to many relationships
): BaseModel()