package com.example.ecommerce_app.core.data.model

typealias URLPath = String

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: URLPath = ""
) {
    constructor(): this("", "", "", "")
}
