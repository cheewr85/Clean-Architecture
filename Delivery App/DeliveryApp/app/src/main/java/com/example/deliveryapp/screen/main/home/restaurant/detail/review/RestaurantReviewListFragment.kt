package com.example.deliveryapp.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import com.example.deliveryapp.data.entity.RestaurantFoodEntity
import com.example.deliveryapp.databinding.FragmentListBinding
import com.example.deliveryapp.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantReviewListFragment: BaseFragment<RestaurantReviewListViewModel,FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantReviewListViewModel>()

    override fun observeData() {

    }

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"


        // 번들로 값을 담고 프래그먼트를 반환하는 함수
        fun newInstance(restaurantId: Long): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}