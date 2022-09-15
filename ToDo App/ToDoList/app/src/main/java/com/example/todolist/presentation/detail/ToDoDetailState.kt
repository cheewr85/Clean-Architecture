package com.example.todolist.presentation.detail

import com.example.todolist.data.entity.ToDoEntity

// ToDoListState와 비슷하게 작동함, 다른 상태가 더 추가됨
// ToDoDetailState를 상속받아 Object나 data class로 활용 가능함
sealed class ToDoDetailState {

    object UnInitialized: ToDoDetailState()

    object Loading: ToDoDetailState()

    data class Success(
        val toDoItem: ToDoEntity
    ): ToDoDetailState()

    object Delete: ToDoDetailState()

    object Modify: ToDoDetailState()

    object Error: ToDoDetailState()

    object Write: ToDoDetailState()

}
