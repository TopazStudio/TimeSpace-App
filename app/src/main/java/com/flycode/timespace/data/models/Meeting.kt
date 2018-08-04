package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "meetings" )
@ManyToMany(referencedTable = Tag::class)
class Meeting: BaseObservable(), Serializable{
    @field: [PrimaryKey Column(name = "id")]
    var id: Int = -1

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("summary") Column(name = "summary")]
    @get: Bindable
    var summary: String = ""
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


    //RELATIONSHIPS

    @ForeignKey(saveForeignKeyModel = true)
    var location: Location? = null

    @ForeignKey(saveForeignKeyModel = true)
    var time : Time? = null

    @ForeignKey()
    var timeTable: TimeTable? = null

    @ForeignKey()
    var owner: User? = null

    var tags: MutableList<Tag> = ArrayList()

    var attendances : MutableList<Attendance> = ArrayList()

    var attendees : MutableList<User> = ArrayList()

    var attachments : MutableList<Document> = ArrayList()


    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "attendances")
    fun getMyAttendances() : List<Attendance>{
        if (attendances.isEmpty())
            attendances = SQLite.select()
                    .from(Attendance::class.java)
                    .where(Attendance_Table.attendable_id.eq(id),Attendance_Table.attendable_type.eq("meeting"))
                    .queryList()
        return attendances
    }

    //TODO: use attendances table to fetch attendees of meeting in where clause
    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "attendees")
    fun getMyAttendees() : List<User>{
        if (attendees.isEmpty())
            attendees = SQLite.select()
                    .from(User::class.java)
                    .where(Attendance_Table.attendable_id.eq(id),Attendance_Table.attendable_type.eq("meeting"))
                    .queryList()
        return attendees
    }

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "attachments")
    fun getMyAttachments() : List<Document>{
        if (attachments.isEmpty())
            attachments = SQLite.select()
                    .from(Document::class.java)
                    .where(Document_Table.documentable_id.eq(id),Document_Table.documentable_type.eq("meeting"))
                    .queryList()
        return attachments
    }

}