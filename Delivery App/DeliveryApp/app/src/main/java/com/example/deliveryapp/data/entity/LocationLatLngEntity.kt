package com.example.deliveryapp.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// 위도 & 경도를 처리하는 Entity
@Parcelize
@androidx.room.Entity
data class LocationLatLngEntity(
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey(autoGenerate = true)
    override val id: Long = -1
): Entity, Parcelable