package com.example.deliveryapp.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.deliveryapp.R
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.MapSearchInfoEntity
import com.example.deliveryapp.databinding.FragmentHomeBinding
import com.example.deliveryapp.screen.base.BaseFragment
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import com.example.deliveryapp.screen.mylocation.MyLocationActivity
import com.example.deliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocationInfo ->
                        // 위치 바뀐 것을 알려주고 그 위치로 로드함
                        viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)
                }
            }
    }

    // 위치권한에 대해서 받아오는 런쳐, 여러 개의 권한을 받아올 수 있게 처리
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 기존에 넣음 권한이 있는지 확인하고 그 값이 유효한지 체크함(filter를 통해)
            val responsePermissions = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            // 권한이 true로 되어 있다면 즉, 포함되어 있다면 권한 설정 된 것이므로 리스너를 등록해주면 됨
            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                // 위치 권한이 없으므로 받아줘야함
                with(binding.locationTitleText) {
                    setText(R.string.please_request_location_permission)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(requireContext(), getString(R.string.can_not_assigned_permission), Toast.LENGTH_SHORT).show()
            }
        }

    override fun initViews() = with(binding) {
        // 현재 위치 값 클릭하면 다른 위치를 등록할 수 있는 Activity로 넘어가게함
        // ViewModel에서 현재 위치 정보가 있는지 확인하고 있을 때 화면 전환을 할 것임
        locationTitleText.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    // 해당 인텐트 실행
                    MyLocationActivity.newIntent(
                        requireContext(),
                        mapInfo
                    )
                )
            }
        }
    }

    // 위치를 불러와 검색을 할 때 씀
    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
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

    override fun observeData() = viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
        // state상태에 따라 데이터 처리(권한이 있으면 위치를 불러옴)
        when(it) {
            is HomeState.Uninitialized -> {
                getMyLocation()
            }
            is HomeState.Loading -> {
                // 로딩중일 때 프로그래스바 처리
                binding.locationLoading.isVisible = true
                binding.locationTitleText.text = getString(R.string.loading)
            }
            is HomeState.Success -> {
                // 성공한 경우, 로딩바를 없애고 위치를 나타내고 ViewPager를 초기화함
                binding.locationLoading.isGone = true
                binding.locationTitleText.text = it.mapSearchInfo.fullAddress
                binding.tabLayout.isVisible = true
                binding.filterScrollView.isVisible = true
                binding.viewPager.isVisible = true
                initViewPager(it.mapSearchInfo.locationLatLng)
            }
            is HomeState.Error -> {
                binding.locationLoading.isGone = true
                binding.locationTitleText.setText(R.string.can_not_load_address_info)
                // 에러가 뜬 경우 다시 위치 정보를 불러오게 함
                binding.locationTitleText.setOnClickListener {
                    getMyLocation()
                }
                Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 위치를 불러오는 함수, 초기화 바탕으로 gps확인하고 없다면 런처를 실행시켜 권한을 받을것임
    private fun getMyLocation() {
        // 초기화가 되어 있지 않으면 위치 정보 권한을 받아오게 처리함
        if (::locationManager.isInitialized.not()) {
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        // 초기화가 되어 있으면 gps가 켜져 있는지 확인
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    // 리스너 등록 처리
    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    // Fragment를 보여주기 위해 객체로 생성
    companion object {

        // 위치 권한 처리, 위치를 받기 위한 권한등록, launch에 활용하기 위한 용용(init 상태에서 위치를 불러옴)
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    // 한 번 리스너가 불리면 더 이상 불러지기 않게 하기 위한 함수
    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    // Location 객체 생성하서 리스너로 받아서 처리하게 GPS와 네트워크 환경을 통해 둘 중 하나로 가져오게 함, 그러기 위한 클래스
    inner class MyLocationListener: LocationListener {
        // 위치 바뀌면 데이터를 처리해주면 됨
        override fun onLocationChanged(location: Location) {
            viewModel.loadReverseGeoInformation(
                // Entity에 위도 & 경도값을 넘겨주면 됨
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            removeLocationListener()
        }
    }
}