package com.example.shoppingapp.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ActivityMainBinding
import com.example.shoppingapp.presentation.BaseActivity
import com.example.shoppingapp.presentation.list.ProductListFragment
import com.example.shoppingapp.presentation.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), BottomNavigationView.OnNavigationItemSelectedListener {

    override val viewModel by inject<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(ProductListFragment(), ProductListFragment.TAG)
    }

    // 화면상에 보여지는 각각의 프래그먼트를 지정할 됨
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 아이템 선택시 해당하는 아이템의 View가 선택되도록 처리함
        return when(item.itemId) {
            R.id.menu_products -> {
                showFragment(ProductListFragment(), ProductListFragment.TAG)
                true
            }
            R.id.menu_profile -> {
                showFragment(ProfileFragment(), ProfileFragment.TAG)
                true
            }
            else -> false
        }
    }

    // 프래그먼트 객체와 태그를 받아 기존에 있으면 보여주게끔 함(태그로 기존 Fragment의 위치를 찾을 수 있음)
    private fun showFragment(fragment: Fragment, tag: String) {
        // 태그를 기준으로 찾아서 처리함
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        // 만약 기존의 Fragment가 Fragment스택에 있다면 for문을 통해 꺼낸 뒤 트랜잭션에서 숨김
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        // 만약 해당 fragment가 있으면 찾아서 보여주면 됨
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            // 없다면 해당 fragment를 추가해줌 그리고 회전시 상태에 대해서 사라지는 것을 허용함
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        // RefreshOrderList인 경우 프로필 탭으로 이동하게 처리함
        when(it) {
            is MainState.RefreshOrderList -> {
                binding.bottomNav.selectedItemId = R.id.menu_profile
                val fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)
                // TODO fragment BaseFragment 타입 캐스팅 fetchData()
            }
        }
    }
}