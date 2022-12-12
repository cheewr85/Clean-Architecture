package com.example.deliveryapp.data.repository.restaurant

import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory

// 인터페이스 기반으로 Repository가 만들어지는 기반을 만듬(공통사항)
interface RestaurantRepository {

    // 특정 타입에 맞는 식당 리스트를 가져옴
    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>
}