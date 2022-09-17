package com.example.shoppingapp.presentation.list

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.example.shoppingapp.databinding.FragmentProductListBinding
import com.example.shoppingapp.extension.toast
import com.example.shoppingapp.presentation.BaseFragment
import com.example.shoppingapp.presentation.adapter.ProductListAdapter
import org.koin.android.ext.android.inject

// BaseFragment에서 View 생성시, ViewBinding 진행과 fetchData가 구현
internal class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    // BottomNav 선택시 구분하기 위한 태그
    companion object {
        const val TAG = "ProductListFragment"
    }

    override val viewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    private val adapter = ProductListAdapter()

    // 인텐트의 결과 코드를 처리하는 것
    private val startProductDetailForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // 성공적으로 처리 완료 이후 동작
        }

    override fun observeData() = viewModel.productListStateLiveData.observe(this) {
        // LiveData 상태를 보고 UI를 갱신함
        when(it) {
            is ProductListState.UnInitialized -> {
                initViews(binding)
            }
            is ProductListState.Loading -> {
                handleLoadingState()
            }
            is ProductListState.Success -> {
                handleSuccessState(it)
            }
            is ProductListState.Error -> {
                handleErrorState()
            }
        }
    }

    private fun initViews(binding: FragmentProductListBinding) = with(binding) {
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ProductListState.Success) = with(binding) {
        refreshLayout.isRefreshing = false

        if (state.productList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setProductList(state.productList) {
//                // Intent로 넘겨받고 해당 콜백을 처리하게 함
//                startProductDetailForResult.launch(
//                    ProductDetailActivity.newIntent(requireContext(), it.id)
//                )
                requireContext().toast("Product Entity : $it")
            }
        }
    }

    private fun handleErrorState() {
        Toast.makeText(requireContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }

}