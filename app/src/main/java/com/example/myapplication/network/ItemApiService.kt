package com.example.myapplication.network

import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {
    @GET("/products")
    suspend fun getItemData(): Response<List<Item>>
}
