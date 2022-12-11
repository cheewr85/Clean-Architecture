package com.example.deliveryapp.data.entity

import android.os.Parcelable
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize

// 식당 데이터를 담을 Entity
@Parcelize
data class RestaurantEntity(
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>
) : Entity, Parcelable