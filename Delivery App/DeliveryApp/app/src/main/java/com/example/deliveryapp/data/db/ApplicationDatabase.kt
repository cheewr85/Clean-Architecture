package com.example.deliveryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.deliveryapp.data.db.dao.FoodMenuBasketDao
import com.example.deliveryapp.data.db.dao.LocationDao
import com.example.deliveryapp.data.db.dao.RestaurantDao
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.data.entity.RestaurantFoodEntity

// RoomDB 필요한 부분 구현
@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao

    abstract fun RestaurantDao(): RestaurantDao

    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao
}