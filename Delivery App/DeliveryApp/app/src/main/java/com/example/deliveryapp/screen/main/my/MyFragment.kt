package com.example.deliveryapp.screen.main.my

import com.example.deliveryapp.databinding.FragmentMyBinding
import com.example.deliveryapp.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    // Fragment를 보여주기 위해 객체로 생성
    companion object {

        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }
}