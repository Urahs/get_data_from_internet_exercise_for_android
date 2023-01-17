package com.example.myapplication.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {
    @GET("/products")
    suspend fun getAllItemsData(): Response<List<Item>>

    @GET("/products/categories")
    suspend fun getCategoriesData(): Response<MutableList<String>>

    @GET("/products/category/{category_name}")
    suspend fun getCategoryItems(@Path("category_name") categoryName: String): Response<List<Item>>
}
