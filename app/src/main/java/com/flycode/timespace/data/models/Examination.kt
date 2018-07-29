package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "examinations" )
@ManyToMany(referencedTable = Tag::class)
class Examination: BaseObservable(), Serializable{
    @field: [PrimaryKey Column(name = "id")]
    var id: Int = 0

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name: String = ""
    set(value) {
        field = value
        notifyChange()
    }

    @field: [SerializedName("note") Column(name = "note")]
    @get: Bindable
    var note: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("type") Column(name = "type")]
    @get: Bindable
    var type: String = ""
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

    @field: [SerializedName("description") Column(name = "description")]
    @get: Bindable
    var description: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    // Relationships

    @ForeignKey(saveForeignKeyModel = true)
    var location: Location? = null

    @ForeignKey(saveForeignKeyModel = true)
    var time: Time? = null

    @ForeignKey()
    var timeTable: TimeTable? = null

    @ForeignKey()
    var owner: User? = null

    @ForeignKey()
    var subject: Clazz? = null

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