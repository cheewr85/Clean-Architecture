package com.example.deliveryapp.di

import com.example.deliveryapp.data.TestOrderRepository
import com.example.deliveryapp.data.TestRestaurantFoodRepository
import com.example.deliveryapp.data.TestRestaurantRepository
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.repository.order.OrderRepository
import com.example.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.example.deliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import com.example.deliveryapp.screen.order.OrderMenuListViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }

    viewModel { (firebaseAuth: FirebaseAuth) -> OrderMenuListViewModel(get(), get(), firebaseAuth) }

    single<RestaurantRepository> { TestRestaurantRepository() }

    single<RestaurantFoodRepository> { TestRestaurantFoodRepository() }

    single<OrderRepository> { TestOrderRepository() }
}