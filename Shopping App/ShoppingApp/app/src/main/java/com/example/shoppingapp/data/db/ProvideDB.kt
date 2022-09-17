package com.example.shoppingapp.data.db

import android.content.Context
import androidx.room.Room

// RoomDB를 Repository에서 쓰게 하기 위해서 의존성 주입을 위한 함수들
internal fun provideDB(context: Context): ProductDatabase =
    Room.databaseBuilder(context, ProductDatabase::class.java, ProductDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ProductDatabase) = database.productDao()