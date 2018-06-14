package com.flycode.musclemax_app.data.models.TypeConverters

import android.databinding.ObservableInt
import com.raizlabs.android.dbflow.converter.TypeConverter

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ObservableIntConverter : TypeConverter<Int, ObservableInt>() {

    override fun getDBValue(model: ObservableInt): Int? {
        return model.get()
    }

    override fun getModelValue(data: Int?): ObservableInt {
        return ObservableInt(data!!)
    }
}
