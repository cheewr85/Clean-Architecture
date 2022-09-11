package com.example.todolist

import android.app.Application
import com.example.todolist.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ToDoListApplication: Application() {

    // 추후 Koin을 통한 의존성 주입을 위해서 만든 부분
    override fun onCreate() {
        super.onCreate()
        // TODO Koin Trigger
        // Koin을 활용한 모듈생성
        startKoin {
            // 에러발생시 로깅할 수 있도록 함
            androidLogger(Level.ERROR)
            androidContext(this@ToDoListApplication)
            // 모듈을 생성해서 넣어줌(DI 폴더에서 진행)
            modules(appModule)
        }
    }
}