package com.example.todolist.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.data.local.db.dao.ToDoDao

// 로컬 DB 사용을 위해 RoomDB와 이와 같이 패키징 진행
@Database(
    entities = [ToDoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDataBase.db"
    }

    abstract fun toDoDao(): ToDoDao
}