package com.example.deliveryapp.data.repository.map

import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.network.MapApiService
import com.example.deliveryapp.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

// MapRepository 구현체
class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
) : MapRepository {

    // MapAPI를 쓰고 결과를 처리함
    override suspend fun getReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity): AddressInfo? =
        withContext(ioDispatcher) {
            val response = mapApiService.getReverseGeoCode(
                lat = locationLatLngEntity.latitude,
                lon = locationLatLngEntity.longitude
            )
            if (response.isSuccessful) {
                response.body()?.addressInfo
            } else {
                null
            }
        }
}