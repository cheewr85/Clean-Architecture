package com.example.deliveryapp.data.network

import com.example.deliveryapp.data.response.address.AddressInfoResponse
import com.example.deliveryapp.data.url.Key
import com.example.deliveryapp.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// TMAP API를 쓰기 위한 service
interface MapApiService {

//    @GET(Url.GET_TMAP_POIS)
//    suspend fun getSearchLocation(
//        @Header("appKey") appKey: String = Key.TMAP_API,
//        @Query("version") version: Int = 1,
//        @Query("callback") callback: String? = null,
//        @Query("count") count: Int = 20,
//        @Query("searchKeyword") keyword: String? = null,
//        @Query("areaLLCode") areaLLCode: String? = null,
//        @Query("areaLMCode") areaLMCode: String? = null,
//        @Query("resCoordType") resCoordType: String? = null,
//        @Query("searchType") searchType: String? = null,
//        @Query("multiPoint") multiPoint: String? = null,
//        @Query("searchtypCd") searchtypCd: String? = null,
//        @Query("radius") radius: String? = null,
//        @Query("reqCoordType") reqCoordType: String? = null,
//        @Query("centerLon") centerLon: String? = null,
//        @Query("centerLat") centerLat: String? = null
//    ): Response<SearchResponse>
//
//    @GET(Url.GET_TMAP_POIS_AROUND)
//    suspend fun getSearchLocationAround(
//        @Header("appKey") appKey: String = Key.TMAP_API,
//        @Query("version") version: Int = 1,
//        @Query("categories") categories: String? = null,
//        @Query("callback") callback: String? = null,
//        @Query("count") count: Int = 20,
//        @Query("searchKeyword") keyword: String? = null,
//        @Query("areaLLCode") areaLLCode: String? = null,
//        @Query("areaLMCode") areaLMCode: String? = null,
//        @Query("resCoordType") resCoordType: String? = null,
//        @Query("searchType") searchType: String? = null,
//        @Query("multiPoint") multiPoint: String? = null,
//        @Query("searchtypCd") searchtypCd: String? = null,
//        @Query("radius") radius: String? = null,
//        @Query("reqCoordType") reqCoordType: String? = null,
//        @Query("centerLon") centerLon: String? = null,
//        @Query("centerLat") centerLat: String? = null
//    ): Response<SearchResponse>

    // Tmap API에 있는 위치 정보를 불러오게 하는 함수, 주소의 정보가 결과로 돌아옴
    @GET(Url.GET_TMAP_REVERSE_GEO_CODE)
    suspend fun getReverseGeoCode(
        @Header("appKey") appKey: String = Key.TMAP_API,
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("coordType") coordType: String? = null,
        @Query("addressType") addressType: String? = null
    ): Response<AddressInfoResponse>
}