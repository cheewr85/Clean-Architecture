package com.example.deliveryapp.screen.main

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ActivityMainBinding
import com.example.deliveryapp.screen.main.home.HomeFragment
import com.example.deliveryapp.screen.main.like.RestaurantLikeListFragment
import com.example.deliveryapp.screen.main.my.MyFragment
import com.example.deliveryapp.util.event.MenuChangeEventBus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun observeData() {
        // 상세화면에서 메인 탭으로 넘어오게 처리하기 위해 Flow 구독처리함
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
    }

    private fun initViews() = with(binding) {
        // navigation 선택시 바뀌는 경우 그를 받기 위한 리스너
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 탭에 따라서 Fragment가 바뀌게 구현함
        return when(item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }
            R.id.menu_like -> {
                showFragment(RestaurantLikeListFragment.newInstance(), RestaurantLikeListFragment.TAG)
                true
            }
            R.id.menu_my -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    // 화면 Container에 보여줄 용도로 Fragment를 처리하는 함수
    private fun showFragment(fragment: Fragment, tag: String) {
        // 만약 Fragment가 존재하면 그대로 보여주고 없으면 새로 만들어서 추가
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        // fragment 교체가 될 때 나머진 숨겨야하므로 전처리함
        // 이때 탭으로 로그인 여부에 따라 옮겨지므로 Loss가 일어날 수 있다고 처리해줘야함
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss() // 손실 허용
        }
    }
}

enum class MainTabMenu(@IdRes val menuId: Int) {

    HOME(R.id.menu_home), LIKE(R.id.menu_like), MY(R.id.menu_my)

}