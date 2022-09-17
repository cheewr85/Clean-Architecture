### ToDo앱
- ToDoList와 새로고침 & 리스트 전체 삭제등 일반적인 ToDoList의 기능을 담음

- ToDo 생성 & 수정 & 삭제도 가능하고 이를 모드로 나눠서 처리를 진행함

- 앱 아키텍처를 따라서 AAC-Repository MVVM 모델을 바탕으로 만듬

- 이를 Clean Architecture를 적용 3가지 레이어로 나눠서 구성함

- Prsentation Layer & Domain Layer & Data Layer로 구별

- 그리고 TDD 기반으로 작성 진행, 각각의 비즈니스 로직을 테스트 코드를 통해 먼저 검증을 한 뒤 메인 기능을 붙이

### 기술 스택
- Clean Architecture 구성

   - data

      - entity

      - local

      - repository

   - domain

      - todo

      - UseCase

   - presentation

      - detail

      - list

      - view

   - di

- MVVM 패턴 구성

   - ViewModel

   - LiveData

   - Repository

   - ViewBinding

- 비동기 작업

   - Coroutine

- 의존성 주입(DI)

   - Koin

- 로컬 DB

   - RoomDB

- TDD

   - mockito

   - koin-test

   - coroutine-test

   - junit

- 새로고침 레이아웃

   - Swipe Refresh Layout

## 레이어 구성

### Data Layer
- 데이터 소스가 담길 데이터 클래스와 RoomDB를 사용할 인터페이스와 클래스들 그리고 이를 쓰게 해주기 위한 Repository로 구성됨

- 여기서 직접적인 data class와 RoomDB에 대해서는 일반적으로 사용하는 방식과 동일하게 진행됨

- 여기서 Repository를 Test Code에서도 활용하기 위해서 인터페이스로 기본 형태를 잡고 구현했기 때문에 좀 더 알아본다면

- 아래와 같이 원형을 만들어두고 Test에서도 쓸 수 있고 실제 프로덕션 코드에서도 활용할 수 있게 만들어둠

~~~kotlin
// Repository 패턴을 통해서 어떠한 함수를 추상화해서 호출해서 주입된 인스턴스에 맞게 불러올 수 있게 하기 위해 인터페이스로 함
interface ToDoRepository {
    // 코루틴 이용 io쓰레드 활용 & 아래 함수에 들어갈 유형은 ToDo Entity가 될 것임
    suspend fun getToDoList():List<ToDoEntity>

    suspend fun insertToDoItem(toDoItem: ToDoEntity): Long

    suspend fun insertToDoList(toDoList: List<ToDoEntity>)

    // 업데이트 성공 여부를 위해 Boolean으로 리턴함
    suspend fun updateToDoItem(toDoItem: ToDoEntity)

    suspend fun getToDoItem(itemId: Long): ToDoEntity?

    suspend fun deleteAll()

    suspend fun deleteToDoItem(id: Long)
}
~~~

- 이 부분에서 실제 DB를 사용하기 위한 Repository에서는 아래와 같이 인터페이스를 구현해서 쓸 수 있음

~~~kotlin
// Test 코드를 위해 구현한 Repository와 별개로 이제 DB 사용에 쓰기 위해서 레포지토리 구현함
class DefaultToDoRepository(
    private val toDoDao: ToDoDao,
    // DB 작업이므로 io 디스패쳐 사용함
    private val ioDispathcer: CoroutineDispatcher
): ToDoRepository {

    // 앞서 생성한 DataBase와 Dao를 활용해서 레포지토리에서 처리하여 사용하게 함
    override suspend fun getToDoList(): List<ToDoEntity> = withContext(ioDispathcer) {
        toDoDao.getAll()
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? = withContext(ioDispathcer) {
        toDoDao.getById(itemId)
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) = withContext(ioDispathcer) {
        toDoDao.insert(toDoList)
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long = withContext(ioDispathcer) {
        toDoDao.insert(toDoItem)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoEntity) = withContext(ioDispathcer) {
        toDoDao.update(toDoItem)
    }

    override suspend fun deleteAll() = withContext(ioDispathcer) {
        toDoDao.deleteAll()
    }

    override suspend fun deleteToDoItem(id: Long) = withContext(ioDispathcer) {
        toDoDao.delete(id)
    }
}
~~~

- 그 다음 Test를 위한 Repository를 보면 아래와 같음, 이는 테스트를 위한 Test Double로 Mock으로써 만들어서 진행함

~~~kotlin
// 테스트 용으로 쓸 레포지토리
class TestToDoRepository: ToDoRepository {
    // 테스트 시 안드로이드는 로컬 캐싱 할 방법이 없으므로 아래와 같이 List객체륾 만들어서 처리함(빈 리스트)
    private val toDoList: MutableList<ToDoEntity> = mutableListOf()

    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) {
        this.toDoList.addAll(toDoList)
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity): Long {
        this.toDoList.add(toDoItem)
        return toDoItem.id
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity) {
        val foundToDoEntity = toDoList.find { it.id == toDoEntity.id }
        this.toDoList[toDoList.indexOf(foundToDoEntity)] = toDoEntity
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? {
        return toDoList.find { it.id == itemId}
    }

    // 컬렉션에서 아예 다 삭제하는 메소드로 비워줌
    override suspend fun deleteAll() {
        toDoList.clear()
    }

    override suspend fun deleteToDoItem(id: Long) {
        val foundToDoEntity = toDoList.find { it.id == id }
        this.toDoList.removeAt(toDoList.indexOf(foundToDoEntity))
    }


}
~~~

- 위와 같이 TDD 진행을 염두해두고 인터페이스 형태로 만들어서 구현을 하게해서 테스트 코드로써 활용함과 동시에 실제 만들때도 활용해서 처리했음

### DI
- 의존성 주입을 위한 것으로 Koin 라이브러리를 사용함, 그래서 아래와 같이 Application 단위로 Koin시작을 진행하고, 사용할 모듈을 넣음

- Application으로 상정해서 어디서든 의존성을 주입할 수 있게 처리하기 위함임

~~~kotlin
class ToDoListApplication: Application() {

    // 추후 Koin을 통한 의존성 주입을 위해서 만든 부분
    override fun onCreate() {
        super.onCreate()
        // TODO Koin Trigger
        // Koin을 활용한 모듈생성
        startKoin {
            // 에러발생시 로깅할 수 있도록 함
            androidLogger(Level.ERROR)
            androidContext(this@ToDoListApplication)
            // 모듈을 생성해서 넣어줌(DI 폴더에서 진행)
            modules(appModule)
        }
    }
}
~~~

- 그리고 아래와 같이 의존성 주입할 객체들을 모듈로써 만들어서 처리함, 이러면 해당 객체가 필요한 곳에서 생성자에 추가함으로써 해당 객체가 주입이 됨

~~~kotlin

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
~~~

- Test Code 작성시 Test에 필요한 모듈 역시 위와 같이 구성해서 처리함

### Domain Layer
- 간단한 비즈니스 로직의 캡슐화를 함, 복잡성을 줄여주고 재사용성을 위해 씀 주로 UseCase를 다룸

- 일반적인 빈 UseCase 인터페이스를 만들고 이를 구현함으로써 각각의 비즈니스 로직을 채움

- 현재 앱에서는 아이템 삽입, 수정, 삭제, 리스트 삽입, 수정, 삭제 그리고 조회에 대한 UseCase를 만듬

- 이 중 하나를 보면 아래와 같이 구성됨, UseCase 모두 아래와 같이 invoke를 통해서 실제 해야할 작업을 처리하게 구성됨

~~~kotlin
// update가 됐는지 테스트 확인 & 하나의 아이템만을 확인하기 위한 UseCase
internal class GetToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    // 함수 호출에서 UseCase의 레포지토리에 처리되게끔 알아서 처리하게 opeator invoke 선언
    suspend operator fun invoke(itemId: Long): ToDoEntity? {
        return toDoRepository.getToDoItem(itemId)
    }
}
~~~
~~~kotlin
internal class GetToDoListUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    // 함수 호출에서 UseCase의 레포지토리에 처리되게끔 알아서 처리하게 opeator invoke 선언
    suspend operator fun invoke(): List<ToDoEntity>{
        return toDoRepository.getToDoList()
    }
}
~~~

- 이를 실제로 사용한다면 아래와 같음

~~~kotlin
// fetch 데이터 호출 시 데이터 변경을 확인, 실제 코루틴 이용시 ViewModelScope를 사용함
    // 해당 Scope에서 특정 함수 동작이 마무리 될 때까지 코루틴 블럭에서 관리하다가 끝나면 해제할 수 있도록 ViewModelScope가 사용됨
    override fun fetchData(): Job = viewModelScope.launch {
        // ToDoListState 로딩중으로 바꿈 & 성공이면 데이터를 받아 처리함
        _toDoListLiveData.postValue(ToDoListState.Loading)
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }
~~~

- 즉, List라는 화면에서 데이터를 불러와야 하는데 이를 ViewModel에서 LiveData를 통해 처리를 하는데 해당 데이터를 불러올 때 UseCase를 사용하면서 그 UseCase는 List를 불러오는 Repository를 호출하고 해당 Repository는 앞서 구현한 DefaultRepository를 Koin을 통해 주입하는 방식을 썼기 때문에 ToDoRepository 인터페이스를 썼어도 Default의 함수가 호출 Item List가 불러와지게 됨

- 삭제 & 수정에 경우도 동일함, 아래와 같이 파라미터를 넘길 수도 있음, 요구사항대로

~~~kotlin
internal class InsertToDoListUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    // 함수 생성, 오퍼레이터 이용해서 구현 가능(함수를 호출할 수 있도록 해줌), 외부에서 넣으면 아래 로직 실행
    suspend operator fun invoke(toDoList: List<ToDoEntity>) {
        return toDoRepository.insertToDoList(toDoList)
    }

}
~~~

- 이처럼 UseCase를 구성하게 되면 Repository의 매우 거대해지는 코드와 처리를 단순화해서 처리할 수 있기 때문에 관심사 분리가 일반적인 MVVM만 썼을 때보다 훨씬 더 개선이 될 수 있음

### Presentation Layer
- UI를 나타내고 처리하는 부분으로써 앞서 data & domain layer에서 해당 View에서 작업할 비즈니스 로직을 처리해주고 UI에 있는 데이터 관리와 갱신만 신경써주면 됨

- 그래서 이 data 관리는 ViewModel에서 LiveData를 바탕으로 domain layer를 통해서 주고받고 처리함, 이 domain layer는 직접적인 처리는 data layer에서 진행을 함

- 여기서 추가적으로 볼 것은 State 패턴을 활용, State Class를 통해서 ViewModel에서 사용하면서 View에서 State 상태에서 변하게끔 처리함

~~~kotlin
// fetch 데이터 호출 시 데이터 변경을 확인, 실제 코루틴 이용시 ViewModelScope를 사용함
    // 해당 Scope에서 특정 함수 동작이 마무리 될 때까지 코루틴 블럭에서 관리하다가 끝나면 해제할 수 있도록 ViewModelScope가 사용됨
    override fun fetchData(): Job = viewModelScope.launch {
        // ToDoListState 로딩중으로 바꿈 & 성공이면 데이터를 받아 처리함
        _toDoListLiveData.postValue(ToDoListState.Loading)
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }
~~~

- 위의 코드에 State가 쓰였는데 실제 State 클래스는 아래와 같음

~~~kotlin
// sealed 클래스(추상클래스) 활용하여 ToDoListState를 상속을 받는 object나 data class를 구성함
// viewmodel에서 State가 반영이 되도록 LiveData에 초기화함
sealed class ToDoListState {

    // 초기화되어 있지 않은 상태
    object UnInitialized: ToDoListState()

    // 로딩중인 상태
    object Loading: ToDoListState()

    // 성공해서 데이터를 불러오는 상태
    data class Success(
        val toDoList: List<ToDoEntity>
    ): ToDoListState()

    // 에러인 케이스
    object Error: ToDoListState()
}
~~~

- 이 상태를 통해 View를 변경하는 것은 아래와 같이 진행할 수 있음

~~~kotlin
override fun observeData() {
        viewModel.todoListLiveData.observe(this) {
            // 상태값에 따라 LiveData 초기화를 다르게 해 줌
            when (it) {
                is ToDoListState.UnInitialized -> {
                    initViews(binding)
                }
                is ToDoListState.Loading -> {
                    handleLoadingState()
                }
                is ToDoListState.Success -> {
                    handleSuccessState(it)
                }
                is ToDoListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    // UI에 대한 변화를 추가해서 처리함
    private fun handleErrorState() {
        Toast.makeText(this, "에러가 발생했습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ToDoListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.toDoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.toDoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setToDoList(
                state.toDoList,
                toDoItemClickListener = {
                    startActivityForResult(
                        DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
                        DetailActivity.FETCH_REQUEST_CODE
                    )
                }, toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }
~~~

- 마지막으로 ViewModel이나 Activity에서 공통적으로 적용해서 쓰는 사항이 있기 때문에 아래와 같이 Base로 만들어두고 상속을 받게 하면 중복을 줄일 수 있음

- 즉 아래의 onCreate의 경우 fetchJob과 observeData 호출이 있는데 이는 실제 ListActivity의 onCreate에서는 별도로 호출할 필요없이 상속 받았기 때문에 기본적으로 실행이 되는 역할을 함(두번째 코드)
~~~kotlin
// 공통적인 사항에 대해서 Base로 묶어서 처리함
internal abstract class BaseActivity<VM: BaseViewModel>: AppCompatActivity() {

    // BaseViewModel을 제네릭으로 받아서 타입을 지정해줌
    abstract val viewModel: VM

    private lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // onCreate에서 fetchJob을 진행함, 진입시 데이터 불러오고
        fetchJob = viewModel.fetchData()
        observeData()
    }

    // 진입할 때마다 데이터 불러오고 UI 변경되도록 하기 위해 추상함수로 구현
    abstract fun observeData()

    // 없어지면 취소되게 함
    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }
}
~~~
~~~kotlin
internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    // 현재 버전에선 inject를 통해서 의존성 주입함
    override val viewModel: ListViewModel by inject()

    private val adapter = ToDoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

....
~~~

- 이는 ViewModel도 동일함

~~~kotlin
// 공통된 요소를 BaseViewModel 하나로 묶음, internal 추상 클래스로함
// 공통으로 있는 함수를 위해서 만듬
// 기본적으로 ViewModel을 상속받은 것이라 구현할 ViewModel클래스에서 상속받아도 ViewModel을 상속은 똑같이 이루어짐
internal abstract class BaseViewModel: ViewModel() {

    // fetchData는 List & Detail 모두 사용하는 함수인데 이렇게 원형만 만들어두고 실제 구현체 ViewModel에 상속하면
    // 공통사항에 대해서 오버라이딩해서 쓸 수 있게끔 개선이 가능함
    abstract fun fetchData(): Job
}
~~~

### TDD
- Test Code의 경우, 각각 생성할 ViewModel에서의 LiveData를 확인하기 위한 UnitTest용으로 만듬

- 이 Test를 기반으로, 비즈니스 로직을 확인하고 이를 바탕으로 해당 ViewModel을 작성함
