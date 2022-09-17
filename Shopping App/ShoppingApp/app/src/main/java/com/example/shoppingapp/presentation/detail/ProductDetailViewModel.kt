package com.example.shoppingapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.entity.product.ProductEntity
import com.example.shoppingapp.domain.GetProductItemUseCase
import com.example.shoppingapp.domain.OrderProductItemUseCase
import com.example.shoppingapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductDetailViewModel(
    private val productId: Long,
    private val getProductItemUseCase: GetProductItemUseCase,
    private val orderProductItemUseCase: OrderProductItemUseCase
) : BaseViewModel() {

    private var _productDetailStateLiveData = MutableLiveData<ProductDetailState>(ProductDetailState.UnInitialized)
    val productDetailStateLiveData: LiveData<ProductDetailState> = _productDetailStateLiveData

    // 주문완료시 해당 Entity 활용
    private lateinit var productEntity: ProductEntity

    // productId 기반으로 API 호출함
    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProductDetailState.Loading)
        getProductItemUseCase(productId)?.let { product ->
            productEntity = product
            setState(
                ProductDetailState.Success(product)
            )
        } ?: kotlin.run {
            setState(ProductDetailState.Error)
        }
    }

    // 주문시 Entity 상태에 따라 State 처리를 진행함
    fun orderProduct() = viewModelScope.launch {
        if (::productEntity.isInitialized) {
            val productId = orderProductItemUseCase(productEntity)
            if (productEntity.id == productId) {
                setState(ProductDetailState.Order)
            }
        } else {
            setState(ProductDetailState.Error)
        }
    }

    // LiveData 상태를 설정하여 State패턴 사용
    private fun setState(state: ProductDetailState) {
        _productDetailStateLiveData.postValue(state)
    }

}