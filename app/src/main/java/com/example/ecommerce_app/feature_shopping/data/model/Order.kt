package com.example.ecommerce_app.feature_shopping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Parcelize
data class Order(
    val id: String = UUID.randomUUID().toString(),
    val cartProductList: List<CartProduct>,
    val totalPrice: Int,
    val address: Address,
    val data: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderStatus: OrderStatus = OrderStatus.Ordered,
) : Parcelable {
    constructor(): this("", emptyList(), 0, Address(), "", OrderStatus.Ordered)
}

