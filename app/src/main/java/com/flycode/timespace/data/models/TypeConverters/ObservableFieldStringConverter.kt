package com.flycode.musclemax_app.data.models.TypeConverters


import com.flycode.musclemax_app.data.models.CustomTypes.ObservableFieldString
import com.raizlabs.android.dbflow.converter.TypeConverter

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ObservableFieldStringConverter : TypeConverter<String, ObservableFieldString>() {

    override fun getDBValue(model: ObservableFieldString): String? {
        return model.get()
    }

    override fun getModelValue(data: String): ObservableFieldString {
        return ObservableFieldString(data)
    }
}
