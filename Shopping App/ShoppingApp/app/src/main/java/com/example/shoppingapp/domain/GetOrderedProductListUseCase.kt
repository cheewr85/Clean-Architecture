package com.example.shoppingapp.domain

import com.example.shoppingapp.data.entity.product.ProductEntity
import com.example.shoppingapp.data.repository.ProductRepository

// Item을 가져오게 하는 UseCase
internal class GetOrderedProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(): List<ProductEntity> {
        return productRepository.getLocalProductList()
    }
}