package com.example.shoppingapp.domain

import com.example.shoppingapp.data.repository.ProductRepository

internal class DeleteOrderedProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke() {
        return productRepository.deleteAll()
    }
}