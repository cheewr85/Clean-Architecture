package com.example.todolist.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.domain.todo.GetToDoListUseCase
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
    private val getToDoListUseCase: GetToDoListUseCase
): ViewModel() {

    // LiveData를 확인해야하기 때문에 이를 선언함, 내외부 사용을 구분하기 위해 아래와 같이 씀
    private var _toDoListLiveData = MutableLiveData<List<ToDoEntity>>()
    val todoListLiveData: LiveData<List<ToDoEntity>> = _toDoListLiveData

    // fetch 데이터 호출 시 데이터 변경을 확인, 실제 코루틴 이용시 ViewModelScope를 사용함
    // 해당 Scope에서 특정 함수 동작이 마무리 될 때까지 코루틴 블럭에서 관리하다가 끝나면 해제할 수 있도록 ViewModelScope가 사용됨
    fun fetchData(): Job = viewModelScope.launch {
        _toDoListLiveData.postValue(getToDoListUseCase())
    }
}