package com.example.shoppingapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppingapp.data.db.dao.ProductDao
import com.example.shoppingapp.data.entity.product.ProductEntity
import com.example.shoppingapp.utillity.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ProductDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ProductDataBase.db"
    }

    abstract fun productDao(): ProductDao
}