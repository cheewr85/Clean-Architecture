package com.example.deliveryapp.screen.main.home

import com.example.deliveryapp.databinding.FragmentHomeBinding
import com.example.deliveryapp.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    // Fragment를 보여주기 위해 객체로 생성
    companion object {

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}