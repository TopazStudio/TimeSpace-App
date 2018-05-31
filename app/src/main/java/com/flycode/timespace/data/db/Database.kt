package com.flycode.timespace.data.db

@com.raizlabs.android.dbflow.annotation.Database(
        name = Database.NAME,
        version = Database.VERSION,
        foreignKeyConstraintsEnforced = true
)
object Database {
    const val NAME = "TimeSpace"
    const val VERSION = 2
}
