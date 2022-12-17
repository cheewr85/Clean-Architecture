package com.example.deliveryapp.model.restaurant.review

import android.net.Uri
import com.example.deliveryapp.model.CellType
import com.example.deliveryapp.model.Model

// Review 화면에 띄우기 위해 처리하는 Model 객체
data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Int,
    val thumbnailImageUri: Uri? = null
): Model(id, type)
