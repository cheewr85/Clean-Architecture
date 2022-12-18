package com.example.deliveryapp.di

import com.example.deliveryapp.data.TestRestaurantRepository
import com.example.deliveryapp.data.entity.LocationLatLngEntity
import com.example.deliveryapp.data.repository.restaurant.RestaurantRepository
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantCategory
import com.example.deliveryapp.screen.main.home.restaurant.RestaurantListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }

    single<RestaurantRepository> { TestRestaurantRepository() }
}