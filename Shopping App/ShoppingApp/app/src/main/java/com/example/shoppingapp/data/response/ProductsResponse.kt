package com.example.shoppingapp.data.response

// 여러개를 받을 때 item과 count를 받는 용도
data class ProductsResponse(
    val items: List<ProductResponse>,
    val count: Int
)
