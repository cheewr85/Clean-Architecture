package com.example.deliveryapp.di

import com.example.deliveryapp.util.provider.DefaultResourcesProvider
import com.example.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    // Network 관련 DI
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single { provideRetrofit(get(), get()) }

    // ResourceProvider DI, Context를 주입함
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    // 코루틴에 필요한 DI
    single { Dispatchers.IO }
    single { Dispatchers.Main }

}