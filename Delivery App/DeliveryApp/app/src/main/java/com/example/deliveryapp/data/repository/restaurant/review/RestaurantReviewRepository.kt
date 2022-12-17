package com.example.deliveryapp.data.repository.restaurant.review

import com.example.deliveryapp.data.entity.RestaurantReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): List<RestaurantReviewEntity>

}