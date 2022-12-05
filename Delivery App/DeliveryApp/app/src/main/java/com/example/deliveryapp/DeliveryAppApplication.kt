package com.example.deliveryapp

import android.app.Application
import android.content.Context
import com.example.deliveryapp.di.appModule
import org.koin.core.context.startKoin

class DeliveryAppApplication : Application() {

    // onCreate시 AppContext를 사용할 것임
    override fun onCreate() {
        super.onCreate()
        appContext = this

        // DI패턴 적용을 위한 의존성 주입
        startKoin {
            modules(appModule)
        }
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