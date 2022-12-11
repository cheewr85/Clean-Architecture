package com.example.deliveryapp.screen.main.home.restaurant

import androidx.annotation.StringRes
import com.example.deliveryapp.R

// 카테고리 처리를 위한 enum class(StringRes를 통해 별도로 저장된 value의 id를 불러와서 처리하기 쉬움)
enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {

    ALL(R.string.all, R.string.all_type)
}