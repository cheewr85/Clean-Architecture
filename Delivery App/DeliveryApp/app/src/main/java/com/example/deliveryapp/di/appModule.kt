package com.example.deliveryapp.di

import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.entity.MapSearchInfoEntity
import com.example.deliveryapp.data.entity.RestaurantEntity
import com.example.deliveryapp.data.entity.RestaurantFoodEntity
import com.example.deliveryapp.data.preference.AppPreferenceManager
import com.example.deliveryapp.data.repository.map.DefaultMapRepository
import com.example.deliveryapp.data.repository.map.MapRepository
import com.example.deliveryapp.data.repository.order.DefaultOrderRepository
import com.example.deliveryapp.data.repository.order.OrderRepository
import com.example.deliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.example.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.example.deliveryapp.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import com.example.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.example.deliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import com.example.deliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import com.example.deliveryapp.data.repository.user.DefaultUserRepository
import com.example.deliveryapp.data.repository.user.UserRepository
import com.example.deliveryapp.screen.main.home.HomeViewModel
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.example.deliveryapp.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.example.deliveryapp.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.example.deliveryapp.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.example.deliveryapp.screen.main.like.RestaurantLikeListViewModel
import com.example.deliveryapp.screen.main.my.MyViewModel
import com.example.deliveryapp.screen.mylocation.MyLocationViewModel
import com.example.deliveryapp.screen.order.OrderMenuListViewModel
import com.example.deliveryapp.util.event.MenuChangeEventBus
import com.example.deliveryapp.util.provider.DefaultResourcesProvider
import com.example.deliveryapp.util.provider.ResourcesProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    // ViewModel DI
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get(), get()) }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) -> RestaurantListViewModel(restaurantCategory, locationLatLng, get()) }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity, get(), get())}
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get(), get())}
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) -> RestaurantMenuListViewModel(restaurantId, restaurantFoodList, get()) }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { (firebaseAuth: FirebaseAuth) -> OrderMenuListViewModel(get(), get(), firebaseAuth) }

    // Repository DI
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get())}
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(), get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }

    // Network ?????? DI
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    // Url??? ???????????? ?????? ???????????? named ?????? ??????, ???????????? ???????????? ??????
    single(named("map")) { provideMapRetrofit(get(), get()) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    // RoomDB ?????? DI
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get())}

    // ResourceProvider DI, Context??? ????????? + Preference ??????????????? ??????
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
    single { AppPreferenceManager(androidApplication()) }

    // ???????????? ????????? DI
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    // Flow
    single { MenuChangeEventBus() }

    // Firebase
    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
}