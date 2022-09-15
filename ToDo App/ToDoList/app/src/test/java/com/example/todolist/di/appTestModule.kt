package com.example.todolist.di

import com.example.todolist.data.repository.TestToDoRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.todo.*
import com.example.todolist.presentation.detail.DetailMode
import com.example.todolist.presentation.detail.DetailViewModel
import com.example.todolist.presentation.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {
    // 의존성 주입을 통해서 사용할 것이므로 필요한 것 넣어둠

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    // 파라미터를 넘겨주기 위해 람다식 활용
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get()
        )
    }

    // UseCase get()을 통해 해당 레포지토리를 가져옴
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }

    // Repository
    single<ToDoRepository> { TestToDoRepository() }

}