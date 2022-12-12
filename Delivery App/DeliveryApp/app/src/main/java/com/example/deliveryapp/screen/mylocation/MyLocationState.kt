package com.example.deliveryapp.screen.mylocation

import androidx.annotation.StringRes
import com.example.deliveryapp.data.entity.MapSearchInfoEntity

// MyLocation에 대한 State 처리
sealed class MyLocationState {

    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    // 위치변경이 된 것을 확인하기 위한 State
    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Error(
        @StringRes val messageId: Int
    ): MyLocationState()
}