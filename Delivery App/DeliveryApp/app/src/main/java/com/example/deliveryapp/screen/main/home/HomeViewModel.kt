package com.example.deliveryapp.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.R
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.MapSearchInfoEntity
import com.example.deliveryapp.data.entity.RestaurantFoodEntity
import com.example.deliveryapp.data.repository.map.MapRepository
import com.example.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.example.deliveryapp.data.repository.user.UserRepository
import com.example.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

// state 패턴 활용
class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }

    // LiveData로 state 관리
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    val foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()

    // Tmap API를 활용해서 위도 경도 기준으로 위차를 불러옴
    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) = viewModelScope.launch {
        // 불러올 때 로딩중 처리함
        homeStateLiveData.value = HomeState.Loading
        // DB에 저장된 유저 정보 가져옴
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity

        // 넘겨받은 결과값 확인, 기존의 위치와 비교하여 내 위치가 맞는지도 확인
        val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfo = info.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame = currentLocation == locationLatLngEntity
            )
        } ?: kotlin.run {
            // 만약 정보가 없다면 에러처리
            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    // 받아둔 위치 정보가 있는지 판별하는 함수
    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfo
            }
        }
        // 만약 없다면 null 반환
        return null
    }

    // 홈화면에서 장바구니에 담긴 것을 확인하기 위해 함수 호출
    fun checkMyBasket() = viewModelScope.launch {
        foodMenuBasketLiveData.value = restaurantFoodRepository.getAllFoodMenuListInBasket()
    }

}