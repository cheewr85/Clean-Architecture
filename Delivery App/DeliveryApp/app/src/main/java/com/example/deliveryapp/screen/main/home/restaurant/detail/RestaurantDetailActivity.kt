package com.example.deliveryapp.screen.main.home.restaurant.detail

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.deliveryapp.R
import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.data.entity.RestaurantFoodEntity
import com.example.deliveryapp.databinding.ActivityRestaurantDetailBinding
import com.example.deliveryapp.extensions.fromDpToPx
import com.example.deliveryapp.extensions.load
import com.example.deliveryapp.screen.base.BaseActivity
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import com.example.deliveryapp.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.example.deliveryapp.screen.main.home.restaurant.detail.review.RestaurantReviewListFragment
import com.example.deliveryapp.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    override fun getViewBinding(): ActivityRestaurantDetailBinding =
        ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    // Intent 처리를 원활하게 하기 위한 객체 생성
    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
            }
    }

    // View 초기화
    override fun initViews() {
        initAppBar()
    }

    // ViewPagerAdapter 초기화
    private lateinit var viewPagerAdapter: RestaurantDetailListFragmentPagerAdapter

    // Appbar 반영, 넘겨받은 값을 바탕으로 appBar의 변경사항을 처리함
    private fun initAppBar() = with(binding) {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // 변화를 계산함, 동적으로 움직이게
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            // Collapsing 되는 상태를 무한정 되지 않게 스크롤 크기를 제한하고 스무스하게 넘기게 처리함
            val realAlphaVerticalOffset: Float =
                if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        toolbar.setNavigationOnClickListener { finish() }
        // 각각 버튼에 따른 기능 구현
        callButton.setOnClickListener {
            // viewModel에서 전화번호 값을 가져옴
            viewModel.getRestaurantTelNumber()?.let { telNumber ->
                // 인텐트로 해당 전화번호로 전화를 걸게끔 처리함
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                startActivity(intent)
            }
        }
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        shareButton.setOnClickListener {
            // restaurant 정보가 있으면 Intent로 담아서 처리함
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                // 공유하기 위한 인텐트 활용
                val intent = Intent(Intent.ACTION_SEND).apply {
                    // text plain의 상수값을 가져오기 위한 타입처리
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "맛있는 음식점 : ${restaurantInfo.restaurantTitle}" +
                                "\n평점 : ${restaurantInfo.grade}" +
                                "\n연락처 : ${restaurantInfo.restaurantTelNumber}"
                    )
                    // 공유하는 모달 띄움
                    Intent.createChooser(this, "친구에게 공유하기")
                }
                startActivity(intent)
            }
        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            // Loading 시 ProgressBar 처리
            is RestaurantDetailState.Loading -> {
                handleLoading()
            }
            is RestaurantDetailState.Success -> {
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        // 성공했을 때 UI 처리
        callButton.isGone = restaurantEntity.restaurantTelNumber == null
        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        // 범위를 표현하기 위해 string 값에 $ 사인을 씀
        deliveryTimeText.text = getString(
            R.string.delivery_expected_time,
            restaurantEntity.deliveryTimeRange.first,
            restaurantEntity.deliveryTimeRange.second
        )
        deliveryTipText.text = getString(
            R.string.delivery_tip_range,
            restaurantEntity.deliveryTipRange.first,
            restaurantEntity.deliveryTipRange.second
        )
        // like에 대해서 Drawable을 불러와서 적용하게끔 state와 함께 처리함
        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ),
            null, null, null
        )

        // ViewPager가 초기화 되어 지 않다면 초기화 진행
        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(state.restaurantEntity.restaurantInfoId, state.restaurantFoodList)
        }
    }

    // 상세화면에서 식당 메뉴에 대해서 담고 있는 ViewPager 초기화
    private fun initViewPager(
        // InfoId와 FoodList는 각각 프래그먼트를 만들어서 처리함
        restaurantInfoId: Long,
        restaurantFoodList: List<RestaurantFoodEntity>?
    ) {
        // ViewPager 초기화 진행
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantInfoId
                )
            )
        )
        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        // tab의 내용을 Category의 따라서 처리함
        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position ->
            tab.setText(RestaurantCategoryDetail.values()[position].categoryNameId)
        }.attach()
    }
}