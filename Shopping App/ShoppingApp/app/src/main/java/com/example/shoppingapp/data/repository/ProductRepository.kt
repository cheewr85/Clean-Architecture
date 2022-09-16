package com.example.shoppingapp.data.repository

import com.example.shoppingapp.data.entity.product.ProductEntity

// API든 DB든 불러오게함 인터페이스로 만듬
interface ProductRepository {

    suspend fun getProductList(): List<ProductEntity>

    suspend fun getLocalProductList(): List<ProductEntity>

    suspend fun insertProductItem(ProductItem: ProductEntity): Long

    suspend fun insertProductList(ProductList: List<ProductEntity>)

    suspend fun updateProductItem(ProductItem: ProductEntity)

    suspend fun getProductItem(itemId: Long): ProductEntity?

    suspend fun deleteAll()

    suspend fun deleteProductItem(id: Long)

}