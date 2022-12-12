package com.example.deliveryapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 위도 & 경도를 처리하는 Entity
@Parcelize
data class LocationLatLngEntity(
    val latitude: Double,
    val longitude: Double,
    override val id: Long = -1
): Entity, Parcelable