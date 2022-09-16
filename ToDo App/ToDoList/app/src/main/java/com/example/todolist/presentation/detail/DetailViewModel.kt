package com.example.todolist.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.domain.todo.DeleteToDoItemUseCase
import com.example.todolist.domain.todo.GetToDoItemUseCase
import com.example.todolist.domain.todo.InsertToDoItemUseCase
import com.example.todolist.domain.todo.UpdateToDoUseCase
import com.example.todolist.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

// 상세 목록에 대해서 처리하기 위한 ViewModel
internal class DetailViewModel(
    var detailMode: DetailMode,
    // 목록에서 받은 id에 따라 다름, 여기서 잘못된 정보가 있을 수 있으므로 디폴트를 1로 함
    var id: Long = -1,
    // 아이템을 가져오는 UseCase 활용
    private val getToDoItemUseCase: GetToDoItemUseCase,
    private val deleteToDoItemUseCase: DeleteToDoItemUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val insertToDoItemUseCase: InsertToDoItemUseCase
): BaseViewModel() {

    private var _toDoDetailLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnInitialized)
    val toDoDetailLiveData: LiveData<ToDoDetailState> = _toDoDetailLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        when (detailMode) {
            // 모드에 따라 불러오는 것을 다르게 처리함
            DetailMode.WRITE -> {
                _toDoDetailLiveData.postValue(ToDoDetailState.Write)
            }
            DetailMode.DETAIL -> {
                // LiveData에 상태를 state패턴을 활용함
                _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
                try {
                    getToDoItemUseCase(id)?.let {
                        // 데이터 성공적으로 불러와 상태값 변경, 그리고 받아온 값을 넘겨받음
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(it))
                    } ?: kotlin.run {
                        // 정보가 없다면 에러가 뜨게함
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }

        }
    }

    // 해당 id에 속하는 Item을 삭제하는 메소드
    fun deleteTodo() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
        try {
            deleteToDoItemUseCase(id)
            _toDoDetailLiveData.postValue(ToDoDetailState.Delete)
        } catch (e: Exception) {
            e.printStackTrace()
            _toDoDetailLiveData.postValue(ToDoDetailState.Error)
        }

    }

    // 업데이트 처리함
    fun writeToDo(title: String, description: String) = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
        when (detailMode) {
            // 모드에 따라 불러오는 것을 다르게 처리함
            DetailMode.WRITE -> {
                // 작성 모드 반영을 위해서 InsertItem이 됨
                try {
                    val toDoEntity = ToDoEntity(
                        title = title,
                        description = description
                    )
                    id = insertToDoItemUseCase(toDoEntity)
                    _toDoDetailLiveData.postValue(ToDoDetailState.Success(toDoEntity))
                    detailMode = DetailMode.DETAIL
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }
            DetailMode.DETAIL -> {
                // 아이템 정보를 가져와서 새로운 객체를 깊은 복사를해서 파라미터로 받은 값으로 갱신함
                try {
                    // 아이템을 가져와 새로운 객체를 만들어서 Update함 성공하면 state 변화를 시킴
                    getToDoItemUseCase(id)?.let {
                        val updateToDoEntity = it.copy(
                            title = title,
                            description = description
                        )
                        updateToDoUseCase(updateToDoEntity)
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(updateToDoEntity))
                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }

        }
    }

    // 수정 모드에 들어갔을 때 쓰는 함수
    fun setModifyMode() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Modify)
    }

}