package com.example.shoppingapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

// BaseViewModel & ViewBinding을 상속받은 타입을 넘겨받아서 처리함
internal abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    // 추상변수로 선언해서 하위 상속받을 때 사용할 수 있게함
    abstract val viewModel: VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    private lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        fetchJob = viewModel.fetchData()
        observeData()
    }

    abstract fun observeData()

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }
}