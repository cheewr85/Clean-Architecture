package com.example.deliveryapp.screen.main.home.restaurant.detail

import android.content.Context
import android.content.Intent
import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.databinding.ActivityRestaurantDetailBinding
import com.example.deliveryapp.screen.base.BaseActivity
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantDetailActivity : BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    override fun getViewBinding(): ActivityRestaurantDetailBinding = ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    // Intent 처리를 원활하게 하기 위한 객체 생성
    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) = Intent(context, RestaurantDetailActivity::class.java).apply {
            putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {

        }
    }
}