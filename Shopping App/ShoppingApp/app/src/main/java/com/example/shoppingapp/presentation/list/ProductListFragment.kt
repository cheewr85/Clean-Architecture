package com.example.shoppingapp.presentation.list

import com.example.shoppingapp.databinding.FragmentProductListBinding
import com.example.shoppingapp.presentation.BaseFragment
import org.koin.android.ext.android.inject

// BaseFragment에서 View 생성시, ViewBinding 진행과 fetchData가 구현
internal class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    // BottomNav 선택시 구분하기 위한 태그
    companion object {
        const val TAG = "ProductListFragment"
    }

    override val viewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    override fun observeData() {
        TODO("Not yet implemented")
    }

}