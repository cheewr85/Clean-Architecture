package com.example.deliveryapp.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Retrofit & OkHttp & API를 위한 DI

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideGsonConvertFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}