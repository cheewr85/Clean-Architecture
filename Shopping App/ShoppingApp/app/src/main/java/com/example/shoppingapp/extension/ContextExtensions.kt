package com.example.shoppingapp.extension

import android.content.Context
import android.widget.Toast

// toast 메시지를 만들기 위해 context를 바탕으로 한 확장함수
internal fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}