package com.example.shoppingapp.presentation.profile

import android.net.Uri
import com.example.shoppingapp.data.entity.product.ProductEntity

sealed class ProfileState {

    object Uninitialized: ProfileState()

    object Loading: ProfileState()

    // 로그인 시 토큰을 받아옴
    data class Login(
        val idToken: String
    ): ProfileState()

    // 로그인 된 상태 안 된 상태를 만들어서 처리할 수 있음
    sealed class Success: ProfileState() {

        // 로그인 시 필요한 정보 담아서 처리함
        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
            val productList: List<ProductEntity> = listOf()
        ): Success()

        object NotRegistered: Success()

    }

    object Error: ProfileState()
}