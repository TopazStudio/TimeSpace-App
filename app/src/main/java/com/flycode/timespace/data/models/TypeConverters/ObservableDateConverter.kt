package com.flycode.musclemax_app.data.models.TypeConverters


import com.flycode.musclemax_app.data.models.CustomTypes.ObservableDate
import com.raizlabs.android.dbflow.converter.TypeConverter
import java.util.*

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ObservableDateConverter : TypeConverter<Long, ObservableDate>() {
    override fun getDBValue(model: ObservableDate?): Long? {
        return if (model == null) null else Objects.requireNonNull<Date>(model.get()).time
    }

    override fun getModelValue(data: Long?): ObservableDate? {
        return if (data == null) null else ObservableDate(Date(data))
    }
}
