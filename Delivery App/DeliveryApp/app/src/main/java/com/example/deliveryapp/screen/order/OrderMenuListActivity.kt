package com.example.deliveryapp.screen.order

import com.example.deliveryapp.databinding.ActivityOrderMenuListBinding
import com.example.deliveryapp.screen.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override fun getViewBinding(): ActivityOrderMenuListBinding = ActivityOrderMenuListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<OrderMenuListViewModel>()

    override fun observeData() {
        TODO("Not yet implemented")
    }
}