package com.example.ecommerce_app.feature_shopping.domain.util

import com.example.ecommerce_app.core.domain.util.Field


data class AddressFields(
    val addressLocation: Field,
    val fullName: Field,
    val street: Field,
    val phoneNumber: Field,
    val cityName: Field,
    val stateName: Field
)