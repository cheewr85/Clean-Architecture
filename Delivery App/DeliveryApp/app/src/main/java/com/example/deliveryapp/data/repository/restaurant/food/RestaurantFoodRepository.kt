package com.example.deliveryapp.data.repository.restaurant.food

import com.example.deliveryapp.data.entity.RestaurantFoodEntity

// 메뉴 리스트를 불러와서 메뉴에 대해서 ViewPager 어댑터에 연결할 것임
interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>
}