package com.example.deliveryapp.util.convertor

import androidx.room.TypeConverter

// Pair를 Room에서 그냥 쓸 수 없기 때문에 타입을 String으로 변환해주는 Converter, 혹은 그 반대도 처리함
object RoomTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toString(pair: Pair<Int, Int>): String {
        return "${pair.first},${pair.second}"
    }

    @TypeConverter
    @JvmStatic
    fun toIntPair(str: String): Pair<Int, Int> {
        val splitedStr = str.split(",")
        return Pair(Integer.parseInt(splitedStr[0]), Integer.parseInt(splitedStr[1]))
    }
}