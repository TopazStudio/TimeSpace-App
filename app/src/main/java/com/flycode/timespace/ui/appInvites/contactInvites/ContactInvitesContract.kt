package com.flycode.timespace.ui.appInvites.contactInvites

import android.content.ContentResolver
import com.flycode.timespace.ui.base.BaseContract

interface ContactInvitesContract {
    interface ContactInvitesFragment : BaseContract.View{
        fun getContentResolver(): ContentResolver
    }

    interface ContactInvitesPresenter<V : ContactInvitesFragment> : BaseContract.Presenter<V>{
        fun fetchContacts()
    }
}