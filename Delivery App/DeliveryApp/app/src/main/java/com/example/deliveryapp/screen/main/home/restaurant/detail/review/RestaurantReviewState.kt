package com.example.deliveryapp.screen.main.home.restaurant.detail.review

import com.example.deliveryapp.data.entity.RestaurantReviewEntity

sealed class RestaurantReviewState {

    object Uninitialized: RestaurantReviewState()

    object Loading: RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewEntity>
    ): RestaurantReviewState()
}
