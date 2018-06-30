package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.timespace.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "users" )
class User : BaseObservable(),Serializable{
    @field: [PrimaryKey Column(name = "id")]
    var id : Int =  0

    @field: [SerializedName("first_name") Column(name = "first_name")]
    @get: Bindable
    var first_name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("second_name") Column(name = "second_name")]
    @get: Bindable
    var second_name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("surname") Column(name = "surname")]
    @get: Bindable
    var surname: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("name_prefix") Column(name = "name_prefix")]
    @get: Bindable
    var name_prefix: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("status") Column(name = "status")]
    @get: Bindable
    var status: Int = 1
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("email") Column(name = "email")]
    @get: Bindable
    var email: String = ""
            set(value) {
        field = value
        notifyChange()
    }

    @field: [SerializedName("password") Column(name = "password")]
    @get: Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyChange()
        }


    @field: [SerializedName("tel") Column(name = "tel")]
    @get: Bindable
    var tel: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("_tag") Column(name = "_tag")]
    @get: Bindable
    var _tag: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    // RELATIONSHIPS
        var pictures : MutableList<Picture> = ArrayList()

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "pictures")
    fun getMyTimes() : List<Picture>?{
        if (pictures.isEmpty())
            pictures = SQLite.select()
                    .from(Picture::class.java)
                    .where(Picture_Table.picturable_id.eq(id),Picture_Table.picturable_type.eq("user"))
                    .queryList()
        return pictures
    }
}