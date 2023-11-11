package com.example.ecommerce_app.feature_auth.domain.util

import com.example.ecommerce_app.core.domain.util.Field

data class LoginFields(
    val email: Field,
    val password: Field
)
