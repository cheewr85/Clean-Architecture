package com.example.deliveryapp.screen.main.home.restaurant

import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.deliveryapp.databinding.FragmentRestaurantListBinding
import com.example.deliveryapp.model.restaurant.RestaurantModel
import com.example.deliveryapp.screen.base.BaseFragment
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.example.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment :
    BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>() {

    // viewModel 호출 시점에 arguments에서 카테고리를 넣어줌
    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
    override val viewModel by viewModel<RestaurantListViewModel> { parametersOf(restaurantCategory) }

    override fun getViewBinding(): FragmentRestaurantListBinding =
        FragmentRestaurantListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    // 식당 리스트를 나타내기 위한 recyclerview adapter 생성
    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantListListener {
                override fun onClickItem(model: RestaurantModel) {
                    Toast.makeText(requireContext(), "$model", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    // LiveData를 구독해서 데이터 처리함
    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        adapter.submitList(it)
    }

    companion object {

        // bundle위한 키값
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"

        // argument로 bundle로 담아서 Fragment를 꺼내주면 됨
        fun newInstance(restaurantCategory: RestaurantCategory): RestaurantListFragment {
            return RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory
                )
            }
        }
    }
}