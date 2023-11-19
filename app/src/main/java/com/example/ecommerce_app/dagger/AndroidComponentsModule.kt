package com.example.ecommerce_app.dagger

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidComponentsModule {
    @Provides
    @Singleton
    fun provideConnectivityManager(
        context: Application
    ) =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    @Provides
    @Singleton
    fun provideContentResolver(context: Application) = context.contentResolver!!
}