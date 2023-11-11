package com.example.ecommerce_app.core.domain.util

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Unspecified<T> : Resource<T>()
}