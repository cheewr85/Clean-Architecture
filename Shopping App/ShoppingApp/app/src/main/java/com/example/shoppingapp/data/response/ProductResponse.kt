package com.example.shoppingapp.data.response

import com.example.shoppingapp.data.entity.product.ProductEntity
import java.util.*

// Json을 그대로 파싱할 수 있도록 하는 역할
data class ProductResponse(
    val id: String,
    val createdAt: Long,
    val productName: String,
    val productPrice: String,
    val productImage: String,
    val productType: String,
    val productIntroductionImage: String
) {
    // Json으로 바꾼 것을 앱에서 쓸 수 있게 Entity형태로 바꿔주는 함수
    fun toEntity(): ProductEntity =
        ProductEntity(
            id = id.toLong(),
            createdAt = Date(createdAt),
            productName = productName,
            productPrice = productPrice.toDouble().toInt(),
            productImage = productImage,
            productType = productType,
            productIntroductionImage = productIntroductionImage
        )
}
