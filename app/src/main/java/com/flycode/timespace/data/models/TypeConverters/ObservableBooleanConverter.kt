package com.flycode.musclemax_app.data.models.TypeConverters

import android.databinding.ObservableBoolean

import com.raizlabs.android.dbflow.converter.TypeConverter

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ObservableBooleanConverter : TypeConverter<Boolean, ObservableBoolean>() {


    override fun getDBValue(model: ObservableBoolean): Boolean? {
        return model.get()
    }

    override fun getModelValue(data: Boolean?): ObservableBoolean {
        return ObservableBoolean(data!!)
    }
}
