package com.example.shoppingapp.di

import com.example.shoppingapp.data.network.buildOkHttpClient
import com.example.shoppingapp.data.network.provideGsonConverterFactory
import com.example.shoppingapp.data.network.provideProductApiService
import com.example.shoppingapp.data.network.provideProductRetrofit
import com.example.shoppingapp.data.repository.DefaultProductRepository
import com.example.shoppingapp.data.repository.ProductRepository
import com.example.shoppingapp.domain.GetProductItemUseCase
import com.example.shoppingapp.presentation.list.ProductListViewModel
import com.example.shoppingapp.presentation.main.MainViewModel
import com.example.shoppingapp.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel() }
    viewModel { ProfileViewModel() }

    // Coroutine Dispatcher
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // UseCases
    factory { GetProductItemUseCase(get()) }

    // Repositories
    // 인터페이스 타입으로 주입받아서 구현을 하게 할 수 있음
    single<ProductRepository> { DefaultProductRepository(get(), get()) }

    // 네트워크 통신을 위해 앞서 만든 함수 총 4가지를 의존성 주입을 준비함
    single { provideGsonConverterFactory() }

    single { buildOkHttpClient() }

    single { provideProductRetrofit(get(), get()) }

    single { provideProductApiService(get()) }
}