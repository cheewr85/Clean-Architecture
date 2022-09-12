package com.example.todolist.data.repository

import com.example.todolist.data.entity.ToDoEntity

// 테스트 용으로 쓸 레포지토리
class TestToDoRepository: ToDoRepository {
    // 테스트 시 안드로이드는 로컬 캐싱 할 방법이 없으므로 아래와 같이 List객체륾 만들어서 처리함(빈 리스트)
    private val toDoList: MutableList<ToDoEntity> = mutableListOf()

    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) {
        this.toDoList.addAll(toDoList)
    }
}