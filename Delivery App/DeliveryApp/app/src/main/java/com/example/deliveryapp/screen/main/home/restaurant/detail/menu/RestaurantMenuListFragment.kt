package com.example.deliveryapp.screen.main.home.restaurant.detail.menu

import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.deliveryapp.data.entity.RestaurantFoodEntity
import com.example.deliveryapp.databinding.FragmentListBinding
import com.example.deliveryapp.model.restaurant.food.FoodModel
import com.example.deliveryapp.screen.base.BaseFragment
import com.example.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.ModelRecyclerAdapter
import com.example.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment: BaseFragment<RestaurantMenuListViewModel,FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val restaurantId by lazy { arguments?.getLong(RESTAURANT_ID_KEY, -1) }
    private val restaurantFoodList by lazy { arguments?.getParcelableArrayList<RestaurantFoodEntity>(FOOD_LIST_KEY)!! }

    override val viewModel by viewModel<RestaurantMenuListViewModel> {
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    // 장바구니 처리를 위해 상위 ViewModel에 접근
    private val restaurantDetailViewModel by sharedViewModel<RestaurantDetailViewModel>()

    private val resourcesProvider by inject<ResourcesProvider>()

    // RecyclerView Adapter 초기화
    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : FoodMenuListListener {
                // 클릭시 장바구니에 추가됨
                override fun onClickItem(model: FoodModel) {
                    viewModel.insertMenuInBasket(model)
                }

            }
        )
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() {

        viewModel.restaurantFoodListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.menuBasketLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "장바구니에 담겼습니다. 메뉴 : ${it.title}", Toast.LENGTH_SHORT).show()
            // 상세화면에 장바구니에 추가했다고 알려주는 함수 호출
            restaurantDetailViewModel.notifyFoodMenuListInBasket(it)
        }

        viewModel.isClearNeedInBasketLiveData.observe(viewLifecycleOwner) { (isClearNeed, afterAction) ->
            if (isClearNeed) {
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed, afterAction)
            }
        }
    }

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"

        const val FOOD_LIST_KEY = "foodList"

        // 번들로 음식점과 거기에 있는 메뉴를 담고 있는 프래그먼트를 반환하는 함수
        fun newInstance(restaurantId: Long, foodList: ArrayList<RestaurantFoodEntity>): RestaurantMenuListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }
}