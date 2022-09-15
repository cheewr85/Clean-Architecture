package com.example.todolist.viewmodel.todo

import com.example.todolist.ViewModelTest
import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.domain.todo.InsertToDoItemUseCase
import com.example.todolist.presentation.detail.DetailMode
import com.example.todolist.presentation.detail.DetailViewModel
import com.example.todolist.presentation.detail.ToDoDetailState
import com.example.todolist.presentation.list.ListViewModel
import com.example.todolist.presentation.list.ToDoListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject


/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test delete todo
 * 4. test update todo
 */
// 만든 DetailViewModel에 대한 테스트 진행
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class DetailViewModelTest: ViewModelTest() {

    // 테스트를 위해 아이디 하나를 가져옴
    private val id = 1L

    // 모드에 따라 달라서 주입시 모드를 파라미터로 추가해줘야함
    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.DETAIL, id) }
    private val listViewModel by inject<ListViewModel>()
    // 해당하는 UseCase 추가
    private val insertToDoItemUseCase: InsertToDoItemUseCase by inject()

    // id값을 받는 엔티티 값을 불러옴
    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    // 다른 과정은 이전 테스트 코드 방식과 유사함
    // 초기화 하는 과정
    @Before
    fun init() {
        initData()
    }

    // 코루틴 활용 위해 임시로 만든 mocking data를 넣고 테스트용 코루틴 활용
    // 하나의 데이터를 추가하는 UeCase 활용
    private fun initData() = runBlockingTest {
        // operator invoke 함수를 구현해서 아래와 같이 mockList만 넣어도 해당 invoke함수 호출해서 Repository를 통해 insert진행
        insertToDoItemUseCase(todo)
    }

    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        // viewModel의 데이터를 테스트를 위해서 만들고 fetch이후 상태를 체크함
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )
    }

    @Test
    fun `test delete todo`() = runBlockingTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()
        // 삭제처리를 하고 검증을 험
        detailViewModel.deleteTodo()
        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Delete
            )
        )

        // 삭제후 리스트에 반영됐는지 확인하기 위해 ListViewModel 씀
        val listTestObservable = listViewModel.todoListLiveData.test()
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

    @Test
    fun `test update todo`() = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()

        val updateTitle = "title 1 update"
        val updateDescription = "descriptiuon 1 update"

        val updateToDo = todo.copy(
            title = updateTitle,
            description = updateDescription
        )

        detailViewModel.writeToDo(
            title = updateTitle,
            description = updateDescription
        )

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(updateToDo)
            )
        )
    }
}