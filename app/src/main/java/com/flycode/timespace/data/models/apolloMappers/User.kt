package com.flycode.timespace.data.models.apolloMappers

import com.flycode.timespace.data.models.*
import com.flycode.timespace.type.*

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

    fun mapClazzToClazzInput(clazz: Clazz): Clazz_Input {
        return Clazz_Input
                .builder()
                .id(clazz.id.toString())
                .owner_id(1.toString())
                .time_table_id(1.toString())
                .name(clazz.name)
                .note("")
                .description("")
                .abbreviation(clazz.abbreviation)
                .color(clazz.color)
                .teacher_name(clazz.teacher_name)
                .build()
    }

    fun mapLocationToLocationInput(location: Location): Location_Input {
        return Location_Input
                .builder()
                .id(location.id.toString())
                .address(location.address)
                .latLng(location.latLng)
                .building(location.building)
                .floor(location.floor)
                .room(location.room)
                .locatable_id(0.toString())
                .locatable_type(" ")
                .build()
    }

    fun mapTimeToTimeInput(time: Time): Time_Input {
        return Time_Input
                .builder()
                .id(time.id.toString())
                .weakly_repeats(time.weekly_repeats.toLong())
                .start_time(time.start_time)
                .end_time(time.end_time)
                .timable_id(0.toString())
                .timable_type(" ")
                .build()
    }

    fun mapExamToExamInput(examination: Examination): Examination_Input {
        return Examination_Input
                .builder()
                .id(examination.id.toString())
                .owner_id(examination.owner?.id.toString())
                .subject_id(examination.subject?.id.toString())
                .time_table_id(1.toString())
                .name(examination.name)
                .type(examination.type)
                .color(examination.color)
                .description(examination.note)
                .note(examination.note)
                .build()
    }
}
