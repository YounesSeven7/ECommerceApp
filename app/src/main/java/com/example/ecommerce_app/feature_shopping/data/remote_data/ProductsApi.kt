package com.example.ecommerce_app.feature_shopping.data.remote_data

import com.example.ecommerce_app.feature_shopping.data.model.Category
import com.example.ecommerce_app.feature_shopping.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @GET("products/?")
    suspend fun getProductsByName(@Query("title") productName: String) : List<Product>

    @GET("products/?")
    suspend fun getProductsByCategoryId(@Query("categoryId") categoryId: Int): List<Product>

    @GET("products/?limit=10")
    suspend fun getProductsByCategoryAndOffset(
        @Query("offset") offset: Int,
        @Query("categoryId") categoryId: Int
    ) : List<Product>


}