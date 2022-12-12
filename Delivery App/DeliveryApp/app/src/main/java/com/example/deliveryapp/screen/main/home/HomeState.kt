package com.example.deliveryapp.screen.main.home

import androidx.annotation.StringRes
import com.example.deliveryapp.data.entity.MapSearchInfoEntity

// 상태 처리를 위한 sealed class
sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInfo: MapSearchInfoEntity,
        // 내 위치가 GPS상 위치와 같은지 체크하는 변수
        val isLocationSame: Boolean
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()

}
