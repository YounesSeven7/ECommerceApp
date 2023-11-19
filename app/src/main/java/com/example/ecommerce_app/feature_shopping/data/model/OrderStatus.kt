package com.example.ecommerce_app.feature_shopping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OrderStatus : Parcelable {
    Ordered,
    Canceled, Confirmed,
    Shipped,
    Delivered,
    Returned
}
