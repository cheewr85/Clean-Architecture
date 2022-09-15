package com.example.todolist.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

// 공통된 요소를 BaseViewModel 하나로 묶음, internal 추상 클래스로함
// 공통으로 있는 함수를 위해서 만듬
// 기본적으로 ViewModel을 상속받은 것이라 구현할 ViewModel클래스에서 상속받아도 ViewModel을 상속은 똑같이 이루어짐
internal abstract class BaseViewModel: ViewModel() {

    // fetchData는 List & Detail 모두 사용하는 함수인데 이렇게 원형만 만들어두고 실제 구현체 ViewModel에 상속하면
    // 공통사항에 대해서 오버라이딩해서 쓸 수 있게끔 개선이 가능함
    abstract fun fetchData(): Job
}