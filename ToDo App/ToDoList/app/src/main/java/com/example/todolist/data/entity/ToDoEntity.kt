package com.example.todolist.data.entity

// ToDo의 내용을 담을 data 클래스
data class ToDoEntity(
    val id: Long = 0,
    val title: String,
    val description: String,
    val hasCompleted: Boolean = false
)
