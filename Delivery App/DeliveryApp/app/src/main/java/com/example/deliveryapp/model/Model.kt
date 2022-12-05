package com.example.deliveryapp.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

// Adapter에 들어갈 초기 상태를 설정하는 Model, RecyclerView가 다양하게 구현되므로 이를 CellType으로 구분 및 상태 설정을 해주는 클래스
abstract class Model(
    open val id: Long,
    open val type: CellType
) {

    companion object {

        // ListAdapter에서 item 변화를 체크하기 위한 DiffUtil, 이를 통해 RecyclerView 아이템에 대해서 효과적으로 처리함
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Model> = object : DiffUtil.ItemCallback<Model>() {
            // 아이템이 같은 값인지 확인
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.id == newItem.id && oldItem.type == newItem.type
            }

            // 아이템 내용이 같은지 객체 자체를 비교함
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem === newItem
            }

        }
    }
}