package com.example.deliveryapp.screen.main.home

import androidx.lifecycle.MutableLiveData
import com.example.deliveryapp.screen.base.BaseViewModel

// state 패턴 활용
class HomeViewModel: BaseViewModel() {

    // LiveData로 state 관리
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)


}