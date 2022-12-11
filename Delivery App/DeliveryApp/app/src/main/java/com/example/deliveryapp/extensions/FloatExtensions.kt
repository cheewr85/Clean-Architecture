package com.example.deliveryapp.extensions

import android.content.res.Resources

// Dp를 Px로 변환하는 확장함수
fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}