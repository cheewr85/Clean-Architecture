package com.example.deliveryapp.widget.adapter.listener.order

import com.example.deliveryapp.widget.adapter.listener.AdapterListener

interface OrderListListener: AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)

}