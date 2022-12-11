package com.example.deliveryapp.model.restaurant

import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.model.CellType
import com.example.deliveryapp.model.Model
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory

// Entity 기반으로 Model을 처리함
data class RestaurantModel(
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>
): Model(id, type) {

    fun toEntity() = RestaurantEntity(
        id,
        restaurantInfoId,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange
    )
}