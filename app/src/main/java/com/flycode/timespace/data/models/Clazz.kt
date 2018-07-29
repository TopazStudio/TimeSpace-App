package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "classes" )
@ManyToMany(referencedTable = Tag::class)
class Clazz: BaseObservable(), Serializable {
    @field: [PrimaryKey Column(name = "id")]
    var id: Int = -1

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name: String = ""
            set(value) {
        field = value
        notifyChange()
    }

    @field: [SerializedName("abbreviation") Column(name = "abbreviation")]
    @get: Bindable
    var abbreviation: String = ""
        set(value) {
            field = value
            notifyChange()
        }


    @field: [SerializedName("color") Column(name = "color")]
    @get: Bindable
    var color: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("teacher_name") Column(name = "teacher_name")]
    @get: Bindable
    var teacher_name: String = ""
        set(value) {
            field = value
            notifyChange()
        }


        // RELATIONSHIPS

    @ForeignKey(saveForeignKeyModel = true)
    var location: Location? = null

    @ForeignKey(saveForeignKeyModel = true)
    var time : Time? = null

    @ForeignKey()
    var timeTable: TimeTable? = null

    @ForeignKey()
    var owner: User? = null

    var tags: MutableList<Tag> = ArrayList()

    var attendances : List<Attendance>? = null

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