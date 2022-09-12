package com.example.todolist.di

import com.example.todolist.data.repository.TestToDoRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.todo.GetToDoListUseCase
import com.example.todolist.domain.todo.InsertToDoListUseCase
import com.example.todolist.presentation.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {
    // 의존성 주입을 통해서 사용할 것이므로 필요한 것 넣어둠

    // ViewModel
    viewModel { ListViewModel(get()) }
    // UseCase get()을 통해 해당 레포지토리를 가져옴
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }

    // Repository
    single<ToDoRepository> { TestToDoRepository() }

}