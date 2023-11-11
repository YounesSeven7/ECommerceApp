package com.example.ecommerce_app.feature_shopping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  Category(
    val id: Int,
    val image: String,
    val name: String
): Parcelable {
    constructor(): this(1, "", "")
}