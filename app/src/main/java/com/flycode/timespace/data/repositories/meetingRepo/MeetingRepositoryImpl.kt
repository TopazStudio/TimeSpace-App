package com.flycode.timespace.data.repositories.meetingRepo

import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.data.network.apollo.MeetingApolloService

class MeetingRepositoryImpl(
        val meetingApolloService: MeetingApolloService
) : MeetingRepository{
    override fun saveMeeting(meeting: Meeting) {
        meetingApolloService.saveMeeting(meeting)
    }

    override fun deleteMeeting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateMeeting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMeeting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMeetings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}