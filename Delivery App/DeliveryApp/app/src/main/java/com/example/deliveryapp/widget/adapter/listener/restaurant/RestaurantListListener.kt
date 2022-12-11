package com.example.deliveryapp.widget.adapter.listener.restaurant

import com.example.deliveryapp.model.restaurant.RestaurantModel
import com.example.deliveryapp.widget.adapter.listener.AdapterListener

// 클릭 이벤트 리스너 처리를 위한 인터페이스
interface RestaurantListListener : AdapterListener {

    fun onClickItem(model: RestaurantModel)

}