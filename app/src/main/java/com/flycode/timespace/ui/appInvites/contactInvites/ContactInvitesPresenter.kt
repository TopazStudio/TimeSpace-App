package com.flycode.timespace.ui.appInvites.contactInvites

import android.provider.ContactsContract
import com.flycode.timespace.data.models.Picture
import com.flycode.timespace.data.models.User
import com.flycode.timespace.ui.base.BasePresenter
import com.flycode.timespace.ui.flexible_items.ExpandableHeaderItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContactInvitesPresenter(
        val mainListAdapter: FlexibleAdapter<ExpandableHeaderItem>
) : BasePresenter<ContactInvitesFragment,ContactInvitesPresenter, ContactInvitesViewModel>(),
        ContactInvitesContract.ContactInvitesPresenter<ContactInvitesFragment> {

    override fun fetchContacts() {
        view?.let { view ->
            view.showLoading()
            compositeDisposable.add(
                Observable.create<ArrayList<User>> { emitter ->
                    val array = ArrayList<User>()
                    val contactsCursor = view.getContentResolver()
                            .query(ContactsContract.Contacts.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null
                            )

                    if (contactsCursor.count > 0){
                        while (contactsCursor.moveToNext()){
                            array.add(
                                User().apply {
                                    this.id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)).toInt()
                                    this.first_name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) ?: "No Name"
                                    contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))?.let {
                                        this.pictures.add(Picture().apply {
                                            this.local_location = it
                                        })
                                    }
                                    if (contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() > 0){
                                        val phoneCursor = view.getContentResolver()
                                                .query(
                                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                        null,
                                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                                        Array(1){this.id.toString()},
                                                        null
                                                )

                                        while (phoneCursor.moveToNext()){
                                            this.tel = this.tel + phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + ","
                                        }

                                        phoneCursor.close()
                                    }
                                }
                            )

                        }
                        contactsCursor.close()
                    }
                    emitter.onNext(array)
                    emitter.onComplete()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    view.hideLoading()
                    viewModel.contactList = it
                    viewModel.contactsFetched = true

                    sendContactsToServer()
                },{
                    view.hideLoading()
                    viewModel.contactsFetched = false
                    if (it.message != null){
                        view.showError(message = it.message.toString())
                    }else{
                        view.showError("Something went wrong. Please try again.")
                    }
                })

            )

        }

    }

    private fun sendContactsToServer() {

    }


}