package org.anvei.novelreader.utils

import androidx.room.TypeConverter
import java.sql.Date

class Converters {

    @TypeConverter
    fun timeStampToDate(timeStamp: Long?): Date?{
        return if (timeStamp == null) null else Date(timeStamp)
    }

    @TypeConverter
    fun dataToTimeStamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}