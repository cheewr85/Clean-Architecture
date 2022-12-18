package com.example.deliveryapp.screen.main.my

import android.net.Uri
import androidx.annotation.StringRes
import com.example.deliveryapp.data.entity.OrderEntity

sealed class MyState {

    object Uninitialized: MyState()

    object Loading: MyState()

    data class Login(
        val idToken: String,
    ): MyState()

    // 성공시, 로그인상태인 경우, 토큰이 없는 경우를 구분함
    sealed class Success: MyState() {

         data class Registered(
             val userName: String,
             val profileImageUri: Uri?,
             val orderList: List<OrderEntity>
         ): Success()

        object NotRegistered: Success()
    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyState()

}
