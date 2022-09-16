package com.example.todolist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job

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