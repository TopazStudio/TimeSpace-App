package com.flycode.timespace.ui.main.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.flycode.timespace.R
import com.flycode.timespace.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_base_nav.*
import kotlinx.android.synthetic.main.time_table_fragment.*
import javax.inject.Inject


class TimeTableFragment
    : BaseFragment<TimeTableFragment, TimeTablePresenter, TimeTableViewModel>(),
        TimeTableContract.TimeTableFragment {

    @Inject override lateinit var viewModel: TimeTableViewModel
    val listeners: MutableList<TimeTableFragmentInterface> = ArrayList()

    companion object {
        fun newInstance() = TimeTableFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_table_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_add.setOnClickListener { onFabAdd() }
        meeting_fab_layout.setOnClickListener {
            closeFabButton()
            NavHostFragment.findNavController(base_nav_fragment).navigate(R.id.MeetingEntryFragment)
        }
        examination_fab_layout.setOnClickListener {
            closeFabButton()
            NavHostFragment.findNavController(base_nav_fragment).navigate(R.id.ExamEntryFragment)
        }
        clazz_fab_layout.setOnClickListener {
            closeFabButton()
            NavHostFragment.findNavController(base_nav_fragment).navigate(R.id.ClassEntryFragment)

        }

        val navHostFragment = childFragmentManager.findFragmentById(R.id.timetable_nav_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(navigation, navHostFragment.navController)
    }

    fun onFabAdd() {
        if (meeting_fab_layout.getVisibility() == View.VISIBLE
                && examination_fab_layout.getVisibility() == View.VISIBLE
                && clazz_fab_layout.getVisibility() == View.VISIBLE) {
            //WHEN VISIBLE
            closeFabButton()
        } else {
            //WHEN HIDDEN
            openFabButton()
        }
    }

    private fun closeFabButton() {
        fab_add.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.anim_fab_button_onhide))

        meeting_fab_layout.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.hide_fab_hidden_buttons))

        examination_fab_layout.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.hide_fab_hidden_buttons))

        clazz_fab_layout.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.hide_fab_hidden_buttons))

        meeting_fab_layout.setVisibility(View.GONE)
        examination_fab_layout.setVisibility(View.GONE)
        clazz_fab_layout.setVisibility(View.GONE)
    }

    private fun openFabButton() {
        meeting_fab_layout.setVisibility(View.VISIBLE)
        examination_fab_layout.setVisibility(View.VISIBLE)
        clazz_fab_layout.setVisibility(View.VISIBLE)
        fab_add.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.anim_fab_button_onshow))
        meeting_fab_layout.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.show_fab_hidden_buttons))
        examination_fab_layout.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.show_fab_hidden_buttons))
        clazz_fab_layout.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.show_fab_hidden_buttons))
    }

    interface TimeTableFragmentInterface{
        fun onFetchComplete()
    }
}
