package com.example.practical62.api

import com.example.practical62.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/products.json")
    suspend fun getProductList(@Query("brand") brand: String): Result<List<Product>>
}