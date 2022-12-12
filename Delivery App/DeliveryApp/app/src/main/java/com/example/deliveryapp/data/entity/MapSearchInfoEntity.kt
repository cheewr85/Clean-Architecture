package com.example.deliveryapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 검색하여서 위치를 처리하기 위한 entity
@Parcelize
data class MapSearchInfoEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
): Parcelable