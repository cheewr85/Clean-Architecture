package com.example.deliveryapp.widget.adapter.viewholder.order

import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ViewholderOrderBinding
import com.example.deliveryapp.model.restaurant.order.OrderModel
import com.example.deliveryapp.screen.base.BaseViewModel
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.listener.AdapterListener
import com.example.deliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding) {
            orderTitleText.text =
                resourcesProvider.getString(R.string.order_history_title, model.orderId)

            val foodMenuList = model.foodMenuList

            // 같은 이름의 경우 합침
            foodMenuList
                .groupBy { it.title }
                .entries.forEach { (title, menuList) ->
                    val orderDataStr =
                        orderContentText.text.toString() + "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"
                    orderContentText.text = orderDataStr
                }
            orderContentText.text = orderContentText.text.trim()

            orderTotalPriceText.text = resourcesProvider.getString(
                R.string.price,
                foodMenuList.map { it.price }.reduce { total, price -> price * total }
            )
        }
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) = Unit
}