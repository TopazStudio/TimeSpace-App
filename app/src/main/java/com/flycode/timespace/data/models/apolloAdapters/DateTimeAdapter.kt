package com.flycode.timespace.data.models.apolloAdapters

import com.apollographql.apollo.CustomTypeAdapter

class DateTimeAdapter: CustomTypeAdapter<Long> {

    // based in part on https://stackoverflow.com/a/10621553/115145

    override fun decode(value: String): Long {
        return value.toLong()

    }

    override fun encode(value: Long): String {
        return value.toString()
    }
}