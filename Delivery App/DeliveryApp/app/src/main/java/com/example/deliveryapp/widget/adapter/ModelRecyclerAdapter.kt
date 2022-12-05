package com.example.deliveryapp.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.deliveryapp.model.CellType
import com.example.deliveryapp.model.Model
import com.example.deliveryapp.screen.base.BaseViewModel
import com.example.deliveryapp.util.mapper.ModelViewHolderMapper
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.example.deliveryapp.widget.adapter.listener.AdapterListener
import com.example.deliveryapp.widget.adapter.viewholder.ModelViewHolder

// List형태로 쓸 수 있는 어댑터 추가, ViewHolder, Listener가 들어있고 공통적으로 관리, 여러 List를 관리할 수 있는 클래스
// 제네릭으로 Model과 ViewModel을 받아서 처리함
class ModelRecyclerAdapter<M: Model, VM: BaseViewModel>(
    private var modelList: List<Model>,
    private val viewModel: VM,
   // ViewHolder의 경우 ViewBinding말고 공통적으로 사용할 수 있는 리소스 Provider사용
    private var resourcesProvider: ResourcesProvider,
    // ViewHolder 구현시 필요한 것
    private val adapterListener: AdapterListener
): ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemCount(): Int = modelList.size

    // 각 타입을 특정 인덱스로 반환하게 함
    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        // 여러가지 ViewHolder를 타입에 맞게 반환할 수 있게 처리함, mapper 객체를 활용함
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourcesProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: MutableList<Model>?) {
        list?.let {
            modelList = it
        }
        super.submitList(list)
    }


}