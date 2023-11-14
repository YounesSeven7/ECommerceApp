package com.example.ecommerce_app.feature_shopping.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment(
    val cartProductsList: List<CartProduct>,
    val price: Int
) : Parcelable