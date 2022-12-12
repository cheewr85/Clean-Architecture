package com.example.deliveryapp.data.repository.user

import com.example.deliveryapp.data.entity.LocationLatLngEntity

interface UserRepository {

    // 위치 정보 변경을 반영하는 함수들
    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)
}