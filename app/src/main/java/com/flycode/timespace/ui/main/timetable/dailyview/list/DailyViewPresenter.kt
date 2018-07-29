package com.flycode.timespace.ui.main.timetable.dailyview.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.timespace.FetchUserActivityQuery
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Clazz
import com.flycode.timespace.data.models.Examination
import com.flycode.timespace.data.models.Meeting
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.ClassSectionableItem
import com.flycode.timespace.ui.flexible_items.ExaminationSectionableItem
import com.flycode.timespace.ui.flexible_items.MeetingListItem
import com.flycode.timespace.ui.flexible_items.PlainHeaderItem
import com.flycode.timespace.ui.main.timetable.TimeTableViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.tudou.library.model.CalendarDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class DailyViewPresenter(
        val apolloClient: ApolloClient,
        val mainListAdapter: FlexibleAdapter<PlainHeaderItem>,
        val superViewModel: TimeTableViewModel
) : BasePresenter<DailyViewList, DailyViewPresenter, DailyViewViewModel>()
        , DailyViewContract.DailyViewPresenter<DailyViewList> {


    private fun prepareGetUserActivity(calendarDay: CalendarDay): ApolloQueryCall<FetchUserActivityQuery.Data> {

        return apolloClient.query(
                FetchUserActivityQuery.builder()
                        .timestamp(calendarDay.time.toDouble())
                        .id(1.toString())
                        .build()
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun fetchItems(){
        view?.let {view ->
                viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(prepareGetUserActivity(view.mCalendarDay))
                            .subscribeOn(Schedulers.io())
                            .flatMap {
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
                                }
                                Observable.just(true)
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({

                                /*CLASSES*/
                                val clazzesListItems =
                                        ArrayList<ClassSectionableItem>().apply {
                                            this.addAll(viewModel.clazzes.map {
                                                ClassSectionableItem(
                                                        viewModel.headingsList[DailyViewList.CLASSES_LIST_POSITION],
                                                        it,
                                                        view.context
                                                )
                                            })
                                        }

                                if(clazzesListItems.isNotEmpty()){
                                    viewModel.headingsList[DailyViewList.CLASSES_LIST_POSITION].apply {
                                        this.addSubItems(0, clazzesListItems as List<ISectionable<*, *>>?)
                                        this.entries = clazzesListItems.size
                                    }

                                }

                                /*EXAMS*/
                                val examinationListItems =
                                        ArrayList<ExaminationSectionableItem>().apply {
                                            this.addAll(viewModel.examinations.map {
                                                ExaminationSectionableItem(
                                                        viewModel.headingsList[DailyViewList.EXAM_LIST_POSITION],
                                                        it,
                                                        view.context
                                                )
                                            })
                                        }

                                if(examinationListItems.isNotEmpty()){
                                    viewModel.headingsList[DailyViewList.EXAM_LIST_POSITION].apply {
                                        this.addSubItems(0, examinationListItems as List<ISectionable<*, *>>?)
                                        this.entries = examinationListItems.size
                                    }

                                }

                                /*MEETINGS*/
                                val meetingsListItems =
                                        ArrayList<MeetingListItem>().apply {
                                            this.addAll(viewModel.meetings.map {
                                                MeetingListItem(
                                                        viewModel.headingsList[DailyViewList.MEETINGS_LIST_POSITION],
                                                        it,
                                                        view.context
                                                )
                                            })
                                        }

                                if(meetingsListItems.isNotEmpty()){
                                    viewModel.headingsList[DailyViewList.MEETINGS_LIST_POSITION].apply {
                                        this.addSubItems(0, meetingsListItems as List<ISectionable<*, *>>?)
                                        this.entries = meetingsListItems.size
                                    }

                                }

                                mainListAdapter.notifyDataSetChanged()
                                mainListAdapter.expandAll()

                                viewModel.uiState.isLoading = false
                            },{
                                viewModel.uiState.isLoading = false
                                viewModel.uiState.onError = true
                                if (it.message != null){
                                    view.showError(it.message.toString())
                                }else{
                                    view.showError(view.resources.getString(R.string.something_went_wrong))
                                }
                            })
            )
        }
    }
}