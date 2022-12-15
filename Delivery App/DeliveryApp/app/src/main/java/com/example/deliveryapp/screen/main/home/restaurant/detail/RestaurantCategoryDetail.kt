package com.example.deliveryapp.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import com.example.deliveryapp.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId: Int
) {

    MENU(R.string.menu), REVIEW(R.string.review)

}