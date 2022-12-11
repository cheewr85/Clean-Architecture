package com.example.deliveryapp.screen.main.home

import com.example.deliveryapp.databinding.FragmentHomeBinding
import com.example.deliveryapp.screen.base.BaseFragment
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import com.example.deliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    override fun initViews() {
        super.initViews()
        initViewPager()
    }

    // 위치를 불러와 검색을 할 때 씀
    private fun initViewPager() = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        // viewpager adapter가 초기화되어 있지 않으면 초기화함
        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList
            )
            viewPager.adapter = viewPagerAdapter
        }

        // tabLayout에서 ViewPager를 뿌림
        // 페이지 바뀔 때마다 한 번 만들어지면 계속 쓸 수 있게함
        viewPager.offscreenPageLimit = restaurantCategories.size
        // TabLayout에 Tab들을 뿌려주게함
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()
    }

    override fun observeData() {

    }

    // Fragment를 보여주기 위해 객체로 생성
    companion object {

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}