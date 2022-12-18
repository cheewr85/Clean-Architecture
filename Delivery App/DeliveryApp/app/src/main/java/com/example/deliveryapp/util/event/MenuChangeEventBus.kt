package com.example.deliveryapp.util.event

import com.example.deliveryapp.screen.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

// Shared Flow를 활용, Intent Bus를 통해서 Intent로 MainTabMenu를 넘겨서 상세화면 종료시키고 마이탭으로 넘기게 처리함
class MenuChangeEventBus {

    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu: MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }

}