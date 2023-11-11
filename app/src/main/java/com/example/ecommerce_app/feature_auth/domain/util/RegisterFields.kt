package com.example.ecommerce_app.feature_auth.domain.util

import com.example.ecommerce_app.core.domain.util.Field

data class RegisterFields(
    val firstName: Field,
    val lastName: Field,
    val email: Field,
    val password: Field
)
