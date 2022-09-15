package com.example.todolist.viewmodel.todo

import com.example.todolist.ViewModelTest
import com.example.todolist.data.entity.ToDoEntity
import com.example.todolist.domain.todo.GetToDoItemUseCase
import com.example.todolist.domain.todo.InsertToDoListUseCase
import com.example.todolist.presentation.list.ListViewModel
import com.example.todolist.presentation.list.ToDoListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

/**
 * [ListViewModel]을 테스트 하기 위한 Unit Test Class
 *
 * 1. initData() -> mocking data를 넣어서 잘 불러오는지 확인
 * 2. test ViewModel fetch
 * 3. test Item update
 * 4. test Item Delete All
 */
// ViewModelTest 상속받음
@ExperimentalCoroutinesApi
internal class ListViewModelTest: ViewModelTest() {

    // 필요한 것을 주입받음(모듈에 이미 선언했으므로 inject를 활용하는 것임)
    private val viewModel: ListViewModel by inject()

    // 임시로 넣을 수 있는 UseCase 만듬
    private val insertToDoListUseCase: InsertToDoListUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    // mocking data를 만듬
    private val mockList = (0 until 10).map {
        ToDoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }
    /**
     * 필요한 UseCase
     * 1. InsertToDoListUseCase
     * 2. GetToDoItemUseCase
     */


    // 초기화 하는 과정
    @Before
    fun init() {
        initData()
    }

    // 코루틴 활용 위해 임시로 만든 mocking data를 넣고 테스트용 코루틴 활용
    private fun initData() = runBlockingTest {
        // operator invoke 함수를 구현해서 아래와 같이 mockList만 넣어도 해당 invoke함수 호출해서 Repository를 통해 insert진행
        insertToDoListUseCase(mockList)
    }

    // 데이터를 불러오기 위한 테스트(백틱을 사용해서 띄어쓰기 상관없게함)
    // Test : test viewModel fetch
    // 입력된 데이터를 불러와서 검증한다.
    @Test
    fun `test viewModel fetch`(): Unit = runTest {
        // 테스트 할 때 잘 들어갔는지 확인 & 앞서 테스트를 위한 LiveData용 확장함수 활용함
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.fetchData()
        // viewmodel에서 fetch이후 testObservable을 통해 mockList와 맞는지 확인
        testObservable.assertValueSequence(
            listOf(
                // state 상태값도 함께 존재
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(mockList)
            )
        )
    }

    // Test : 데이터를 업데이트 했을 때 잘 반영되는가
    @Test
    fun `test Item Update`(): Unit = runBlockingTest {
        // 체크박스에 특정 아이템 완료 체크를 의미함 boolean true
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        // getToDoItemUseCase를 통해서 가져온 아이템과 업데이트 된 Entity를 비교하여 갱신됐는지 테스트함
        // 그렇게 갱신된 값이 정상적으로 반영이 되었는지 확인함
        assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false == todo.hasCompleted)
    }

    // Test : 데이터를 다 날렸을 때 빈상태로 보여지는가
    @Test
    fun `test Item Delete All`(): Unit = runBlockingTest {
        // fetch를 통해 아래의 데이터가 있다가 전체 삭제를 했을 때 비어있는지 검증
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                // state로 반영함
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

}