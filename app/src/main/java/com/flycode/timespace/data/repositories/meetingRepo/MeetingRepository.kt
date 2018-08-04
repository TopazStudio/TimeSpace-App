package com.flycode.timespace.data.repositories.meetingRepo

import com.flycode.timespace.data.models.Meeting

/**
 * Repository dealing with the meeting model
 * */
interface MeetingRepository {
    fun saveMeeting(meeting: Meeting)
    fun deleteMeeting()
    fun updateMeeting()
    fun getMeeting()
    fun getMeetings()
}