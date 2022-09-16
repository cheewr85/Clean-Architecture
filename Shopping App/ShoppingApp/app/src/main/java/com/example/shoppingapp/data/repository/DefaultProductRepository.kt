package com.example.shoppingapp.data.repository

import com.example.shoppingapp.data.entity.product.ProductEntity
import com.example.shoppingapp.data.network.ProductApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

// 의존성 주입을 추가함, 네트워크 통신 & 코루틴 사용 위한 디스패처 & DB에 대해 추가
class DefaultProductRepository(
    private val productApi: ProductApiService,
    private val ioDispatcher: CoroutineDispatcher
): ProductRepository {
    override suspend fun getProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun insertProductItem(ProductItem: ProductEntity): Long = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun insertProductList(ProductList: List<ProductEntity>) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProductItem(ProductItem: ProductEntity) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductItem(itemId: Long): ProductEntity? = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductItem(id: Long) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}