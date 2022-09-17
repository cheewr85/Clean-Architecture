package com.example.shoppingapp.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductListViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch {

    }

}