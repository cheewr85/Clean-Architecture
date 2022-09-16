package com.example.shoppingapp.data.entity.product

import java.util.*

// API를 통해서 아래의 데이터를 받아오므로 그에 맞게 구성함
/**
 {
 "id": "1",
 "createdAt": "2021-04-23T19:44:11.1022",
 "product_name": "Handcrafted Fresh Keyboard",
 "product_price": "263.00",
 "product_image": "http://lorempixel.com/640/480/technics",
 "product": "Bike",
 "product_introduction_image": "http://..."
 },
 */
data class ProductEntity(
 val id: Long,
 val createdAt: Date,
 val productName: String,
 val productPrice: Int,
 val productImage: String,
 val productType: String,
 val productIntroductionImage: String
)
