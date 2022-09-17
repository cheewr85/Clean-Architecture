package com.example.shoppingapp.utillity
import androidx.room.TypeConverter
import java.util.*

// Date를 변환하는 유틸리티 클래스 쉽게 저장하기 위해서
object DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

}