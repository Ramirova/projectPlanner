package com.example.projectplanner.data.db.models

import android.graphics.Color
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun colorToLong(color: Color?): Long? {
        return color?.pack()
    }

    @TypeConverter
    fun longToColor(colorLong: Long): Color {
        return Color.valueOf(colorLong)
    }
}