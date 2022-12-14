package com.example.shoppingapp.di

import com.example.shoppingapp.data.db.provideDB
import com.example.shoppingapp.data.db.provideToDoDao
import com.example.shoppingapp.data.network.buildOkHttpClient
import com.example.shoppingapp.data.network.provideGsonConverterFactory
import com.example.shoppingapp.data.network.provideProductApiService
import com.example.shoppingapp.data.network.provideProductRetrofit
import com.example.shoppingapp.data.preference.PreferenceManager
import com.example.shoppingapp.data.repository.DefaultProductRepository
import com.example.shoppingapp.data.repository.ProductRepository
import com.example.shoppingapp.domain.*
import com.example.shoppingapp.domain.DeleteOrderedProductListUseCase
import com.example.shoppingapp.domain.GetOrderedProductListUseCase
import com.example.shoppingapp.domain.GetProductItemUseCase
import com.example.shoppingapp.domain.GetProductListUseCase
import com.example.shoppingapp.domain.OrderProductItemUseCase
import com.example.shoppingapp.presentation.detail.ProductDetailViewModel
import com.example.shoppingapp.presentation.list.ProductListViewModel
import com.example.shoppingapp.presentation.main.MainViewModel
import com.example.shoppingapp.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get(), get())}

    // Coroutine Dispatcher
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // UseCases
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }
    factory { GetOrderedProductListUseCase(get()) }
    factory { DeleteOrderedProductListUseCase(get()) }

    // Repositories
    // ??????????????? ???????????? ??????????????? ????????? ?????? ??? ??? ??????
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }

    // ???????????? ????????? ?????? ?????? ?????? ?????? ??? 4????????? ????????? ????????? ?????????
    single { provideGsonConverterFactory() }

    single { buildOkHttpClient() }

    single { provideProductRetrofit(get(), get()) }

    single { provideProductApiService(get()) }

    // preferences
    single { PreferenceManager(androidContext()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }
}
