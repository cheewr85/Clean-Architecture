package com.example.deliveryapp.model.restaurant.order

import com.example.deliveryapp.data.entity.OrderEntity
import com.example.deliveryapp.data.entity.RestaurantFoodEntity
import com.example.deliveryapp.model.CellType
import com.example.deliveryapp.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>
): Model(id, type) {

    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList
    )
}
