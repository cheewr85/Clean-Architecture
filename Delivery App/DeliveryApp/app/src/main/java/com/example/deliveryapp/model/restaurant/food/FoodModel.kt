package com.example.deliveryapp.model.restaurant.food

import com.example.deliveryapp.model.CellType
import com.example.deliveryapp.model.Model

// LiveData로 받아서 ViewModel로 쓰기 위한 데이터 클래스
data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long
): Model(id, type)
