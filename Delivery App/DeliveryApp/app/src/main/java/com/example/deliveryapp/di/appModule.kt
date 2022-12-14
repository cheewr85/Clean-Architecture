package com.example.deliveryapp.di

import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.MapSearchInfoEntity
import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.data.repository.map.DefaultMapRepository
import com.example.deliveryapp.data.repository.map.MapRepository
import com.example.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.example.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.example.deliveryapp.data.repository.user.DefaultUserRepository
import com.example.deliveryapp.data.repository.user.UserRepository
import com.example.deliveryapp.screen.main.home.HomeViewModel
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.example.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.example.deliveryapp.screen.main.my.MyViewModel
import com.example.deliveryapp.screen.mylocation.MyLocationViewModel
import com.example.deliveryapp.util.provider.DefaultResourcesProvider
import com.example.deliveryapp.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ViewModel DI
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) -> RestaurantListViewModel(restaurantCategory, locationLatLng, get()) }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity, get(), get())}
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get())}

    // Repository DI
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get())}

    // Network 관련 DI
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single { provideMapRetrofit(get(), get()) }
    single { provideMapApiService(get())}

    // RoomDB 관련 DI
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }

    // ResourceProvider DI, Context를 주입함
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    // 코루틴에 필요한 DI
    single { Dispatchers.IO }
    single { Dispatchers.Main }

}