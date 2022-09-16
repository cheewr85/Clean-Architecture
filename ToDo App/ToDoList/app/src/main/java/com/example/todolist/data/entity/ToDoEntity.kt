package com.example.todolist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// ToDo의 내용을 담을 data 클래스 + DB 사용 위해 어노테이션 추가

@Entity
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val hasCompleted: Boolean = false
)
