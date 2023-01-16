package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {
    @GET("/products")
    suspend fun getItemData(): Response<List<Item>>
}
