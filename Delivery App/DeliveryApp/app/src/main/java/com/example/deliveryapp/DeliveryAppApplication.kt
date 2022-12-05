package com.example.deliveryapp

import android.app.Application
import android.content.Context

class DeliveryAppApplication : Application() {

    // onCreate시 AppContext를 사용할 것임
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    // 삭제시 없앰
    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }

}