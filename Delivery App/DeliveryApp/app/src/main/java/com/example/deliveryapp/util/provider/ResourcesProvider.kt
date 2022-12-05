package com.example.deliveryapp.util.provider

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

// Resource를 가져오는 기능을 구현한 인터페이스
interface ResourcesProvider {

    // Resource로 가져올 수 있는 함수
    fun getString(@StringRes resId: Int): String

    // 여러 데이터를 가져오는 함수
    fun getString(@StringRes resId: Int, vararg formArgs: Any): String

    // Color를 가져오는 함수
    fun getColor(@ColorRes resId: Int): Int

    fun getColorStateList(@ColorRes resId: Int): ColorStateList

}