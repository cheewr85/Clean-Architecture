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

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long {
        this.toDoList.add(toDoItem)
        return toDoItem.id
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity) {
        val foundToDoEntity = toDoList.find { it.id == toDoEntity.id }
        this.toDoList[toDoList.indexOf(foundToDoEntity)] = toDoEntity
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? {
        return toDoList.find { it.id == itemId}
    }

    // 컬렉션에서 아예 다 삭제하는 메소드로 비워줌
    override suspend fun deleteAll() {
        toDoList.clear()
    }

    override suspend fun deleteToDoItem(id: Long) {
        val foundToDoEntity = toDoList.find { it.id == id }
        this.toDoList.removeAt(toDoList.indexOf(foundToDoEntity))
    }


}