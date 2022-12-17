package com.example.deliveryapp.widget.adapter.viewholder.food

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ViewholderFoodMenuBinding
import com.example.deliveryapp.extensions.clear
import com.example.deliveryapp.extensions.load
import com.example.deliveryapp.model.restaurant.food.FoodModel
import com.example.deliveryapp.screen.base.BaseViewModel
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.listener.AdapterListener
import com.example.deliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import com.example.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class FoodMenuViewHolder(
    private val binding: ViewholderFoodMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    // View에 대해서 초기화할 부분 처리함
    override fun reset() = with(binding) {
        foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding) {
            foodImage.load(model.imageUrl, 24f, CenterCrop())
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourcesProvider.getString(R.string.price, model.price)
        }
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        // FoodMenuList일 경우 해당 model을 어댑터 리스너에 넣어주면 됨
        if (adapterListener is FoodMenuListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}