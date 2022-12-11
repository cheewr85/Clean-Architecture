package com.example.deliveryapp.di

import com.example.deliveryapp.data.repository.DefaultRestaurantRepository
import com.example.deliveryapp.data.repository.RestaurantRepository
import com.example.deliveryapp.screen.main.home.HomeViewModel
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListFragment
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.example.deliveryapp.screen.main.my.MyViewModel
import com.example.deliveryapp.util.provider.DefaultResourcesProvider
import com.example.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ViewModel DI
    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel(restaurantCategory, get()) }

    // Repository DI
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }

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