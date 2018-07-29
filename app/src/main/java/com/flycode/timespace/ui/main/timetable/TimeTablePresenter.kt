package com.flycode.timespace.ui.main.timetable

import com.apollographql.apollo.ApolloClient
import com.flycode.timespace.ui.base.BasePresenter

class TimeTablePresenter(
        val apolloClient: ApolloClient
) : BasePresenter<TimeTableFragment, TimeTablePresenter, TimeTableViewModel>(),
        TimeTableContract.TimeTablePresenter<TimeTableFragment> {


}