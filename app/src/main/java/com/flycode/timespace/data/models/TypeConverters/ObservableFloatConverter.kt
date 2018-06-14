package com.flycode.musclemax_app.data.models.TypeConverters


import android.databinding.ObservableFloat

import com.raizlabs.android.dbflow.converter.TypeConverter

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ObservableFloatConverter : TypeConverter<Float, ObservableFloat>() {
    override fun getDBValue(model: ObservableFloat): Float? {
        return model.get()
    }

    override fun getModelValue(data: Float?): ObservableFloat {
        return ObservableFloat(data!!)
    }
}
