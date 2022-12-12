package com.example.deliveryapp.data.repository.map

import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.response.address.AddressInfo

// map관련 API를 위한 Repository
interface MapRepository {

    suspend fun getReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity): AddressInfo?

}