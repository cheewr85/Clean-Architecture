package com.example.deliveryapp.widget.adapter.listener.restaurant

import com.example.deliveryapp.model.restaurant.RestaurantModel

interface RestaurantLikeListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)
}