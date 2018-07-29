package com.flycode.timespace.ui.main.entries.examEntry

import com.flycode.timespace.ui.base.BaseContract

interface ExamEntryContract {
    interface ExamEntryFragment : BaseContract.View{

    }

    interface ExamEntryPresenter<V : ExamEntryFragment> : BaseContract.Presenter<V>{

    }
}