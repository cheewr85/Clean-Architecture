package com.example.todolist.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.domain.todo.DeleteAllToDoItemUseCase
import com.example.todolist.domain.todo.GetToDoListUseCase
import com.example.todolist.domain.todo.UpdateToDoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// 내부 모듈에서만 사용하기 위해 internal씀
/**
 * 필요한 UseCase
 * 1. [GetToDoListUseCase]
 * 2. [UpdateToDoUseCase]
 * 3. [DeleteAllToDoItemUseCase]
 */
internal class ListViewModel(
    // 필요한 UseCase를 주입받아서 씀
    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): ViewModel() {

    // LiveData를 확인해야하기 때문에 이를 선언함, 내외부 사용을 구분하기 위해 아래와 같이 씀
    // ToDoLisState로 초기 설정함
    private var _toDoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val todoListLiveData: LiveData<ToDoListState> = _toDoListLiveData

    // fetch 데이터 호출 시 데이터 변경을 확인, 실제 코루틴 이용시 ViewModelScope를 사용함
    // 해당 Scope에서 특정 함수 동작이 마무리 될 때까지 코루틴 블럭에서 관리하다가 끝나면 해제할 수 있도록 ViewModelScope가 사용됨
    fun fetchData(): Job = viewModelScope.launch {
        // ToDoListState 로딩중으로 바꿈 & 성공이면 데이터를 받아 처리함
        _toDoListLiveData.postValue(ToDoListState.Loading)
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }

    // ViewModel에서 Entity를 업데이트함(이와 관련한 UseCase를 만듬)
    fun updateEntity(todoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoUseCase(todoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
        // LiveData도 갱신함 빈 값으로(State로 반영)
        deleteAllToDoItemUseCase()
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }
}