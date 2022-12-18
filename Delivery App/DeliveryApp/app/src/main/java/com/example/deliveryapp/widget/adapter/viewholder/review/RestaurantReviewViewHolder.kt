package com.example.deliveryapp.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ViewholderRestaurantReviewBinding
import com.example.deliveryapp.extensions.clear
import com.example.deliveryapp.extensions.load
import com.example.deliveryapp.model.restaurant.review.RestaurantReviewModel
import com.example.deliveryapp.screen.base.BaseViewModel
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.listener.AdapterListener
import com.example.deliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import com.example.deliveryapp.widget.adapter.viewholder.ModelViewHolder

// RestaurantReviewList에서 Item으로 보여질 View에 대한 설정(RestaurantReviewList RecyclerView의 ItemViewHolder 설정)
class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }

    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        // data에 대해서 ViewHolder에 연결
        // Mapper를 통해 필요한 것을 받아서 처리함
        with(binding) {
            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString())
            } else {
                reviewThumbnailImage.isGone = true
            }
            reviewTitleText.text = model.title
            reviewText.text = model.description

            ratingBar.rating = model.grade
        }
    }

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = Unit

}