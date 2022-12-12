package com.example.deliveryapp.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.R
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.MapSearchInfoEntity
import com.example.deliveryapp.data.repository.map.MapRepository
import com.example.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.launch

// state 패턴 활용
class HomeViewModel(
    private val mapRepository: MapRepository
): BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }

    // LiveData로 state 관리
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    // Tmap API를 활용해서 위도 경도 기준으로 위차를 불러옴
    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) = viewModelScope.launch {
        // 불러올 때 로딩중 처리함
        homeStateLiveData.value = HomeState.Loading
        // 넘겨받은 결과값 확인
        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfo = info.toSearchInfoEntity(locationLatLngEntity)
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

}