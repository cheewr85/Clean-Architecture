package com.example.deliveryapp.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

// 제너릭으로 받아서 ViewModel을 받게함, ViewBinding도 마찬가지
abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding>: Fragment() {

    abstract val viewModel: VM

    // onCreate이후 view에 대한 처리를 위해서 아래와 같이 선언
    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    // fetchData를 생명주기에 맞게 제거하기 위한 변수
    private lateinit var fetchJob: Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
    }

    // onCreate시 호출할 함수, 상태값 & View 초기화 함수
    open fun initState() {
        // Fragment 상태값 관리를 위해 storeState사용
        arguments?.let{
            viewModel.storeState(it)
        }
        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() = Unit

    // 데이터 변환이 일어날 때 LiveData를 관찰해서 처리하기 위한 함수
    abstract fun observeData()

    override fun onDestroy() {
        // Destroy시 Job 초기화
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }
}