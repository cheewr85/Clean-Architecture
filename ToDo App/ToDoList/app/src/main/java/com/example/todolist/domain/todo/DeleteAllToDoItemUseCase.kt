package com.example.todolist.domain.todo

import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.UseCase

internal class DeleteAllToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    // 함수 호출에서 UseCase의 레포지토리에 처리되게끔 알아서 처리하게 opeator invoke 선언
    suspend operator fun invoke() {
        return toDoRepository.deleteAll()
    }
}