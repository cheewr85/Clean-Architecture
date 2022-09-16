package com.example.todolist.data.repository

import com.example.todolist.data.entity.ToDoEntity

// Repository 패턴을 통해서 어떠한 함수를 추상화해서 호출해서 주입된 인스턴스에 맞게 불러올 수 있게 하기 위해 인터페이스로 함
/**
 * 1.insertToDoList
 * 2.getToDoList
 * 3.updateToDoItem
 */
interface ToDoRepository {
    // 코루틴 이용 io쓰레드 활용 & 아래 함수에 들어갈 유형은 ToDo Entity가 될 것임
    suspend fun getToDoList():List<ToDoEntity>

    suspend fun insertToDoItem(toDoItem: ToDoEntity): Long

    suspend fun insertToDoList(toDoList: List<ToDoEntity>)

    // 업데이트 성공 여부를 위해 Boolean으로 리턴함
    suspend fun updateToDoItem(toDoItem: ToDoEntity)

    suspend fun getToDoItem(itemId: Long): ToDoEntity?

    suspend fun deleteAll()

    suspend fun deleteToDoItem(id: Long)
}