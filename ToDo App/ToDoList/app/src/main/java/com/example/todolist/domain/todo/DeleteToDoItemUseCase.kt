package com.example.todolist.domain.todo

import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.UseCase

// update가 됐는지 테스트 확인 & 하나의 아이템만을 제거하기 위한 UseCase
internal class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    // 함수 호출에서 UseCase의 레포지토리에 처리되게끔 알아서 처리하게 opeator invoke 선언
    // 삭제여부 판단을 위해 boolean 사용
    suspend operator fun invoke(itemId: Long) {
        return toDoRepository.deleteToDoItem(itemId)
    }
}