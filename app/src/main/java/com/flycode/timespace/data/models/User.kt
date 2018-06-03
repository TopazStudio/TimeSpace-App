package com.flycode.timespace.data.models

import android.databinding.BaseObservable
import com.flycode.timespace.data.db.Database
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = (Database::class), name = "users" )
class User : BaseObservable(){
    @PrimaryKey
    @Column(name = "id")
//    @get: Bindable
    var id : Int =  0 /*by bindable(0, BR.id)*/

    @Column(name = "first_name")
//    @get: Bindable
    var first_name: String = ""/*by bindable("", BR.first_name)*/

    @Column(name = "second_name")
//    @get: Bindable
    var second_name: String = ""/*by bindable("", BR.second_name)*/

    @Column(name = "surname")
//    @get: Bindable
    var surname: String = ""/*by bindable("", BR.surname)*/

    @Column(name = "status")
//    @get: Bindable
    var status: String = ""/*by bindable("", BR.status)*/

    @Column(name = "email")
//    @get: Bindable
    var email: String = ""/*by bindable("", BR.email)*/

    @Column(name = "password")
//    @get: Bindable
    var password: String = ""/*by bindable("", BR.email)*/

    @Column(name = "role")
//    @get: Bindable
    var role: String = ""/*by bindable("", BR.role)*/

    @Column(name = "tel")
//    @get: Bindable
    var tel: String = ""/*by bindable("", BR.tel)*/
}