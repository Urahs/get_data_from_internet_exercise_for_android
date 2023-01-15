package com.example.myapplication

import retrofit2.http.GET

//private const val BASE_URL ="https://fakestoreapi.com"

interface ItemApiService {
    @GET("/products")
    suspend fun getItemData(): List<Item>
}
