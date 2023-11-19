package com.example.ecommerce_app.core.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias URLPath = String
@Parcelize
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: URLPath = ""
) : Parcelable {
    constructor(): this("", "", "", "")
}
