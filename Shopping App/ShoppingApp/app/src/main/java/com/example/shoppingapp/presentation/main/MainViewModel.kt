package com.example.shoppingapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MainViewModel: BaseViewModel() {
    override fun fetchData(): Job = viewModelScope.launch {

    }

    // main에서 주문리스트 갱신을 확인함
    private var _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData: LiveData<MainState> = _mainStateLiveData

    fun refreshOrderList() = viewModelScope.launch{
        _mainStateLiveData.postValue(MainState.RefreshOrderList)
    }
}