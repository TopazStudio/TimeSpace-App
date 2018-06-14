package com.flycode.timespace.data.models

import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = (Database::class), name = "meetings" )
@ManyToMany(referencedTable = Tag::class)
data class Meeting(
    @PrimaryKey(autoincrement = true)
    @Column()
    var id: Int = -1,

    @Column()
    var name: String = "",

    @Column()
    var note: String = "",

    @Column()
    var color: Int = 0,

    @Column()
    var description: String = "",

    //RELATIONSHIPS

    @ForeignKey(saveForeignKeyModel = true)
    var location: Location? = null,

    var times : List<Time>? = null,

    @ForeignKey()
    var timeTable: TimeTable? = null,

    @ForeignKey()
    var owner: User? = null,

    var attendances : List<Attendance>? = null

): BaseModel(){

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "times")
    fun getMyTimes() : List<Time>?{
        if (times == null)
            times = SQLite.select()
                    .from(Time::class.java)
                    .where(Time_Table.timable_id.eq(id),Time_Table.timable_type.eq("meeting"))
                    .queryList()
        return times
    }

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "attendances")
    fun getMyAttendances() : List<Attendance>?{
        if (attendances == null)
            attendances = SQLite.select()
                    .from(Attendance::class.java)
                    .where(Attendance_Table.attendable_id.eq(id),Attendance_Table.attendable_type.eq("meeting"))
                    .queryList()
        return attendances
    }

}