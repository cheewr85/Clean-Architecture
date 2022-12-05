package com.example.deliveryapp.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// 공통으로 사용이 될만한 것들을 정의함
abstract class BaseViewModel : ViewModel() {

    // 내부적으로 데이터 관리
    protected var stateBundle: Bundle? = null

    // 공통적으로 호출했을 때 데이터를 가공할 수 있게 만드는 함수, 상속을 받아서 자유롭게 사용할 것임
    open fun fetchData(): Job = viewModelScope.launch {  }

    // View에 대한 형태를 저장하는 함수, Activity나 Fragment에서 생명주기가 종료되기 전까진 state를 관리함
    open fun storeState(stateBundle: Bundle) {
        this.stateBundle = stateBundle
    }
}