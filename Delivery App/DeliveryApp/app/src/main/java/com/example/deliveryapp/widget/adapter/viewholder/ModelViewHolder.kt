package com.example.deliveryapp.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.deliveryapp.model.Model
import com.example.deliveryapp.screen.base.BaseViewModel
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.listener.AdapterListener

// ViewHolder를 공통으로 처리하기 위해 만든 추상 클래스
abstract class ModelViewHolder<M: Model>(
    binding: ViewBinding,
    protected val viewModel: BaseViewModel,
    protected val resourcesProvider: ResourcesProvider
): RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model: M) {
        // 초기화가 필요한 경우를 감안해서 reset을 넣음
        reset()
    }

    // 이벤트 핸들링에 관해서 핸들링에 대한 특정 동작을 구현한 리스너에서 처리할 수 있으므로 아래와 같이 구현
    abstract fun bindViews(model: M, adapterListener: AdapterListener)
}