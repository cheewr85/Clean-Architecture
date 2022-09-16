package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.local.db.ToDoDatabase
import com.example.todolist.data.repository.DefaultToDoRepository
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.todo.*
import com.example.todolist.domain.todo.DeleteAllToDoItemUseCase
import com.example.todolist.domain.todo.DeleteToDoItemUseCase
import com.example.todolist.domain.todo.GetToDoItemUseCase
import com.example.todolist.domain.todo.GetToDoListUseCase
import com.example.todolist.domain.todo.InsertToDoItemUseCase
import com.example.todolist.domain.todo.InsertToDoListUseCase
import com.example.todolist.domain.todo.UpdateToDoUseCase
import com.example.todolist.presentation.detail.DetailMode
import com.example.todolist.presentation.detail.DetailViewModel
import com.example.todolist.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// 내부 패키지에서만 사용할 것이라 internal 사용
internal val appModule = module {

    // 의존성 주입을 통해서 사용할 것이므로 필요한 것 넣어둠
    // 앞서 테스트 코드 사용을 위해 주입한 것을 똑같이 실제 사용을 위해 주입함함

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    // 파라미터를 넘겨주기 위해 람다식 활용
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
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
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }

    // DB
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }

}
// Dao와 DB를 사용할 수 있게 하기 위한 함수
internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()