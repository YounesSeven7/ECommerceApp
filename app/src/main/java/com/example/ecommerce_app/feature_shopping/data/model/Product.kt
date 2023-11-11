package com.example.ecommerce_app.feature_shopping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val category: Category,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String
): Parcelable {
    constructor() : this(Category(), "", 1, emptyList(), 1, "")
}