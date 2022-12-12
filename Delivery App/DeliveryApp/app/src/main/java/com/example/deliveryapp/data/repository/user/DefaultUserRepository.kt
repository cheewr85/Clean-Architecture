package com.example.deliveryapp.data.repository.user

import com.example.deliveryapp.data.db.dao.LocationDao
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

// UserRepository 구현체
class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
): UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(ioDispatcher) {
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity) = withContext(ioDispatcher) {
        locationDao.insert(locationLatLngEntity)
    }
}