package com.example.deliveryapp.screen.main.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.example.deliveryapp.model.restaurant.RestaurantModel
import com.example.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLng: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository
): BaseViewModel() {

    // repository에서 LiveData로 받아서 처리함
    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    // viewModel을 불러올 때 데이터를 뿌려줌, 카테고리 받아서 처리함
    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLng)
        restaurantListLiveData.value = restaurantList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange
            )
        }
    }

    fun setLocationLatLng(locationLatLng: LocationLatLngEntity) {
        // ViewPager내의 위치가 바뀜을 알림
        this.locationLatLng = locationLatLng
        fetchData()
    }
}