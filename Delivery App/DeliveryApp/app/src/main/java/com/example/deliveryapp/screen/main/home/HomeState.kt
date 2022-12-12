package com.example.deliveryapp.screen.main.home

// 상태 처리를 위한 sealed class
sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    object Success: HomeState()

}
