package com.example.ecommerce_app.dagger

import android.app.Application
import coil.ImageLoader
import com.example.ecommerce_app.feature_shopping.data.ProductsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductsApi(): ProductsApi = Retrofit.Builder()
        .baseUrl("https://api.escuelajs.co/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductsApi::class.java)

    @Provides
    @Singleton
    fun provideCoilLoader(context: Application) = ImageLoader.Builder(context)
        .crossfade(true)
        .build()
}