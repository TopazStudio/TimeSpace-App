package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "groups" )
class Group : BaseObservable(), Serializable {
    @field: [PrimaryKey Column(name = "id")]
    var id: Int = 0

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name : String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("description") Column(name = "description")]
    @get: Bindable
    var description : String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("created_at") Column(name = "created_at")]
    @get: Bindable
    var created_at : Long = 0
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("updated_at") Column(name = "updated_at")]
    @get: Bindable
    var updated_at : Long = 0
        set(value) {
            field = value
            notifyChange()
        }

    // RELATIONSHIPS

    @ForeignKey(saveForeignKeyModel = true)
    var owner: User? = User()

    @ForeignKey(saveForeignKeyModel = true)
    var organization: Organization? = null

//    var group_members : MutableList<User> = ArrayList()
    var pictures : MutableList<Picture> = ArrayList()

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "pictures")
    fun getMyPictures() : List<Picture>{
        if (pictures.isEmpty())
            pictures = SQLite.select()
                    .from(Picture::class.java)
                    .where(Picture_Table.picturable_id.eq(id),Picture_Table.picturable_type.eq("user"))
                    .queryList()
        return pictures
    }
}