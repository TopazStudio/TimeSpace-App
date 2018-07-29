package com.flycode.timespace.ui.main.timetable.weeklyview

import android.graphics.Color
import com.alamkanak.weekview.WeekViewEvent
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.FetchUserActivityQuery
import com.flycode.timespace.data.models.Clazz
import com.flycode.timespace.data.models.Examination
import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.main.timetable.TimeTableViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.internal.operators.observable.BlockingObservableNext
import java.util.*

class WeeklyViewPresenter(
        val apolloClient: ApolloClient,
        val superViewModel: TimeTableViewModel
) : BasePresenter<WeeklyViewFragment,WeeklyViewPresenter,WeeklyViewViewModel>() ,
        WeeklyViewContract.WeeklyViewPresenter<WeeklyViewFragment> {

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent>{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH,newMonth - 1)
        calendar.set(Calendar.YEAR,newYear)
        fetchItems(calendar)
        return viewModel.weekViewEvents
    }

    private fun prepareGetUserActivity(calendar: Calendar): ApolloQueryCall<FetchUserActivityQuery.Data> {

        return apolloClient.query(
                FetchUserActivityQuery.builder()
                        .timestamp(calendar.timeInMillis.toDouble())
                        .id(1.toString())
                        .build()
        )
    }

    private fun fetchItems(calendar: Calendar){
        view?.let {view ->
            viewModel.uiState.isLoading = true
                BlockingObservableNext(Rx2Apollo.from(prepareGetUserActivity(calendar))).forEach {
                        it.data()?.userDailyActivity?.let {

                            it.meetingsOwned()?.let {
                                viewModel.meetings.addAll(
                                        Gson().fromJson(
                                                Gson().toJson(it),
                                                object : TypeToken<MutableList<Meeting>>() {}.type
                                        )
                                )
                            }

                            it.attendingMeetings()?.let {
                                viewModel.meetings.addAll(
                                        Gson().fromJson(
                                                Gson().toJson(it),
                                                object : TypeToken<MutableList<Meeting>>() {}.type
                                        )
                                )
                            }

                            it.examinationsOwned()?.let {
                                viewModel.examinations.addAll(
                                        Gson().fromJson(
                                                Gson().toJson(it),
                                                object : TypeToken<MutableList<Examination>>() {}.type
                                        )
                                )
                            }

                            it.attendingExaminations()?.let {
                                viewModel.examinations.addAll(
                                        Gson().fromJson(
                                                Gson().toJson(it),
                                                object : TypeToken<MutableList<Examination>>() {}.type
                                        )
                                )
                            }

                            it.clazzesOwned()?.let {
                                viewModel.clazzes.addAll(
                                        Gson().fromJson(
                                                Gson().toJson(it),
                                                object : TypeToken<MutableList<Clazz>>() {}.type
                                        )
                                )
                            }

                            it.attendingClazzes()?.let {
                                viewModel.clazzes.addAll(
                                        Gson().fromJson(
                                                Gson().toJson(it),
                                                object : TypeToken<MutableList<Clazz>>() {}.type
                                        )
                                )
                            }

                            viewModel.weekViewEvents.addAll(
                                    viewModel.meetings.map {
                                        WeekViewEvent().apply {
                                            this.color = Color.parseColor(it.color)
                                            this.name = it.name
                                            this.location = "building: ${it.location?.building} floor: ${it.location?.floor} room: ${it.location?.room}"
                                            this.startTime = Calendar.getInstance().apply {
                                                this.time = Date(it.time?.start_time!!)
                                            }
                                            this.endTime = Calendar.getInstance().apply {
                                                this.time = Date(it.time?.end_time!!)
                                            }
                                            this.id = it.id.toLong()
                                        }
                                    }
                            )

                            viewModel.weekViewEvents.addAll(
                                    viewModel.examinations.map {
                                        WeekViewEvent().apply {
                                            this.color = Color.parseColor(it.color)
                                            this.name = it.name
                                            this.location = "building: ${it.location?.building} floor: ${it.location?.floor} room: ${it.location?.room}"
                                            this.startTime = Calendar.getInstance().apply {
                                                this.time = Date(it.time?.start_time!!)
                                            }
                                            this.endTime = Calendar.getInstance().apply {
                                                this.time = Date(it.time?.end_time!!)
                                            }
                                            this.id = it.id.toLong()
                                        }
                                    }
                            )

                            viewModel.weekViewEvents.addAll(
                                    viewModel.clazzes.map {
                                        WeekViewEvent().apply {
                                            this.color = Color.parseColor(it.color)
                                            this.name = it.name
                                            this.location = "building: ${it.location?.building} floor: ${it.location?.floor} room: ${it.location?.room}"
                                            this.startTime = Calendar.getInstance().apply {
                                                this.time = Date(it.time?.start_time!!)
                                            }
                                            this.endTime = Calendar.getInstance().apply {
                                                this.time = Date(it.time?.end_time!!)
                                            }
                                            this.id = it.id.toLong()
                                        }
                                    }
                            )
                        }
                    }
            viewModel.uiState.isLoading = false
        }
    }
}