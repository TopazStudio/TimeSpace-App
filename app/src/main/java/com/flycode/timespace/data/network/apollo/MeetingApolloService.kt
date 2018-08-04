package com.flycode.timespace.data.network.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.AddMeetingMutation
import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.data.models.apolloMappers.UserMapper
import com.flycode.timespace.type.Tagged_Input
import io.reactivex.Observable

class MeetingApolloService(
        val apolloClient: ApolloClient
) {
    private fun prepareSaveMeeting(meeting: Meeting): ApolloMutationCall<AddMeetingMutation.Data> {
        return apolloClient.mutate(
                AddMeetingMutation.builder()
                        .meeting(UserMapper.mapMeetingToMeetingInput(meeting))
                        .location(UserMapper.mapLocationToLocationInput(meeting.location!!))
                        .taggeds(
                                meeting.tags.map {
                                    Tagged_Input
                                            .builder()
                                            .id(0.toString())
                                            .tag_id(it.id.toString())
                                            .taggable_id(meeting.id.toString())
                                            .taggable_type("meeting")
                                            .build()
                                }
                        )
                        .time(
                                UserMapper.mapTimeToTimeInput(meeting.time!!)
                        )
                        .documents(
                                meeting.attachments.map {
                                    UserMapper.mapDocumentToDocumentInput(it)
                                }
                        )
                        .users(
                                meeting.attendees.map {
                                    UserMapper.mapUserToUserInput(it)
                                }
                        )
                        .build()
        )
    }

    fun saveMeeting(meeting: Meeting): Observable<Response<AddMeetingMutation.Data>> {
        return Rx2Apollo.from(prepareSaveMeeting(meeting))
    }

}