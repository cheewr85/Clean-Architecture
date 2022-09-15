package com.example.todolist.presentation.detail

// 일반 모드와 글 작성 모드를 구분하기 위한 Enum타입의 클래스
// 이 모드에 따라서 똑같은 화면이어도 쓰는 것을 구현하는지 일반 모드인지 구분이 됨
enum class DetailMode {
    DETAIL, WRITE
}