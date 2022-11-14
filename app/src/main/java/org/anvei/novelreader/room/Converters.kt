package org.anvei.novelreader.room

import androidx.room.TypeConverter
import org.anvei.novelreader.novel.website.WebsiteIdentifier
import java.io.File
import java.sql.Date

class Converters {

    @TypeConverter
    fun timeStampToDate(timeStamp: Long?): Date?{
        return if (timeStamp == null) null else Date(timeStamp)
    }

    @TypeConverter
    fun dataToTimeStamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toWebsiteIdentifier(website: String): WebsiteIdentifier {
        return WebsiteIdentifier.getIdentifier(website)!!
    }

    @TypeConverter
    fun websiteIdentifierToString(identifier: WebsiteIdentifier): String {
        return identifier.name
    }

    @TypeConverter
    fun toFile(cache: String?): File? {
        return if (cache == null) null else File(cache)
    }

    @TypeConverter
    fun fileToString(file: File?): String? {
        return file?.absolutePath
    }
}