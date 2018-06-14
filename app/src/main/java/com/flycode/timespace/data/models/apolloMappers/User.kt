package com.flycode.timespace.data.models.apolloMappers

import com.flycode.timespace.data.models.Picture
import com.flycode.timespace.data.models.User
import com.flycode.timespace.type.Picture_Input
import com.flycode.timespace.type.User_Input

object UserMapper{
    fun mapUserToUserInput(user: User): User_Input {
        return User_Input
                .builder()
                .id(user.id.toString())
                .first_name(user.first_name)
                .second_name(user.second_name)
                .surname(user.surname)
                .email(user.email)
                .tel(user.tel)
                .status(user.status.toLong())
                .build()
    }

    fun mapPictureToPictureInput(picture: Picture): Picture_Input {
        return Picture_Input
                .builder()
                .id(picture.id.toString())
                .name(picture.name)
                .description(picture.description)
                .type(picture.type)
                .size(picture.size.toLong())
                .remote_location(picture.remote_location)
                .picturable_id(picture.picturable_id.toString())
                .picturable_type(picture.picturable_type)
                .build()
    }
}
