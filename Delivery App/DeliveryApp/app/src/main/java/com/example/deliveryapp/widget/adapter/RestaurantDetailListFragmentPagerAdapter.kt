package com.example.deliveryapp.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListFragment

// fragment를 넘겨줘서 attach되는 형태로 Pager에 적용됨
class RestaurantDetailListFragmentPagerAdapter(
    activity: FragmentActivity,
    val fragmentList: List<Fragment>
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}