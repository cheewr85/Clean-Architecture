package com.example.todolist.data.repository

import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.data.local.db.dao.ToDoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

// Test 코드를 위해 구현한 Repository와 별개로 이제 DB 사용에 쓰기 위해서 레포지토리 구현함
class DefaultToDoRepository(
    private val toDoDao: ToDoDao,
    // DB 작업이므로 io 디스패쳐 사용함
    private val ioDispathcer: CoroutineDispatcher
): ToDoRepository {

    // 앞서 생성한 DataBase와 Dao를 활용해서 레포지토리에서 처리하여 사용하게 함
    override suspend fun getToDoList(): List<ToDoEntity> = withContext(ioDispathcer) {
        toDoDao.getAll()
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? = withContext(ioDispathcer) {
        toDoDao.getById(itemId)
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) = withContext(ioDispathcer) {
        toDoDao.insert(toDoList)
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long = withContext(ioDispathcer) {
        toDoDao.insert(toDoItem)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoEntity) = withContext(ioDispathcer) {
        toDoDao.update(toDoItem)
    }

    override suspend fun deleteAll() = withContext(ioDispathcer) {
        toDoDao.deleteAll()
    }

    override suspend fun deleteToDoItem(id: Long) = withContext(ioDispathcer) {
        toDoDao.delete(id)
    }
}