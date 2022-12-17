package com.example.deliveryapp.widget.adapter.listener.restaurant

import com.example.deliveryapp.model.restaurant.food.FoodModel
import com.example.deliveryapp.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {

    fun onClickItem(model: FoodModel)
}