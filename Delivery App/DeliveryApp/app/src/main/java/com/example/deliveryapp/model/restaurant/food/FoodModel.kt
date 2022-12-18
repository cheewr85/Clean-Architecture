package com.example.deliveryapp.model.restaurant.food

import com.example.deliveryapp.data.entity.RestaurantFoodEntity
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
    val restaurantId: Long,
    val foodId: String,
    val restaurantTitle: String
): Model(id, type) {

    // 식당 상세화면에서 나오는 음식들, 장바구니에 담을 수 있게 인덱스를 통해 여러개 담을 수 있도록 함
    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}",
        title,
        description,
        price,
        imageUrl,
        restaurantId,
        restaurantTitle
    )
}
