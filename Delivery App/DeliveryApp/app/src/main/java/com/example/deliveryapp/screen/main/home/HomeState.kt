package com.example.deliveryapp.screen.main.home

import androidx.annotation.StringRes
import com.example.deliveryapp.data.entity.MapSearchInfoEntity

// 상태 처리를 위한 sealed class
sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInfo: MapSearchInfoEntity
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()

}
