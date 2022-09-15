package com.example.todolist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.todolist.di.appTestModule
import com.example.todolist.livedata.LiveDataTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

// 내부 패키지 테스트를 위해 internal 선언
// Koin으로 테스트 + 실험용을 명시하는 어노테이션을 붙임
// 상속을 받게 하기 위해 abstract로 만듬
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal abstract class ViewModelTest: KoinTest {

    // Junit어노테이션 사용 & mokcito도 사용
    // 룰을 정의해서 TDD 준비를 함
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRUle = InstantTaskExecutorRule()

    // mock 데이터 추가, test를 위한 데이터
    @Mock
    private lateinit var context: Application

    // 코루틴 테스트를 위한 사전 작업(쓰레드를 쉽게 바꾸기 위한 디스패처 선언, 코루틴 테스트 시 해당 디스패처 써서 쓰레드 전환이 용이함 + 테스트용)
    private val dispatcher = TestCoroutineDispatcher()

    // 테스트 코드 작성 전 셋업
    @Before
    fun setup() {
        // 똑같이 의존성 주입해서 테스트 실행 준비(Koin 사용 + 모듈 추가)
        startKoin {
            androidContext(context)
            modules(appTestModule)
        }
        Dispatchers.setMain(dispatcher)
    }

    // test 끝난 이후 koin을 멈추고 코루틴을 멈춤
    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain() // MainDispatcher를 초기화 해주어야 메모리 누수가 발생하지 않음
    }

    // LiveData 내부는 제너릭으로 그리고 LiveData 인스턴스가 들어갈 때 이를 검증하는 테스트
    protected fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {
        val testObserver = LiveDataTestObserver<T>()
        // android livedata를 넣어서 테스트
        observeForever(testObserver)

        return testObserver
    }


}