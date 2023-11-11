package com.example.ecommerce_app.feature_shopping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct (
    val product: Product,
    var quantity: Int
) : Parcelable {
    constructor() : this(Product(), 1)
}