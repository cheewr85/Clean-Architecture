package com.example.todolist.presentation.list

import com.example.todolist.data.entity.ToDoEntity

// sealed 클래스(추상클래스) 활용하여 ToDoListState를 상속을 받는 object나 data class를 구성함
// viewmodel에서 State가 반영이 되도록 LiveData에 초기화함
sealed class ToDoListState {

    // 초기화되어 있지 않은 상태
    object UnInitialized: ToDoListState()

    // 로딩중인 상태
    object Loading: ToDoListState()

    // 성공해서 데이터를 불러오는 상태
    data class Success(
        val toDoList: List<ToDoEntity>
    ): ToDoListState()

    // 에러인 케이스
    object Error: ToDoListState()
}