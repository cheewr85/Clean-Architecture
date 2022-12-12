package com.example.deliveryapp.data.entity

// 검색하여서 위치를 처리하기 위한 entity
data class MapSearchInfoEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
)