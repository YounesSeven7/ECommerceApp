package com.example.ecommerce_app.feature_auth.domain.use_case.login

import android.app.Application
import com.example.ecommerce_app.core.domain.use_case.fields.EmailFieldUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.PasswordFieldUseCase
import com.example.ecommerce_app.feature_auth.domain.util.LoginFields
import javax.inject.Inject

class LoginFieldsUseCase @Inject constructor(
    private val emailFieldUseCase: EmailFieldUseCase,
    private val passwordFieldUseCase: PasswordFieldUseCase
) {
    operator fun invoke(email: String, password: String) = LoginFields(
        email = emailFieldUseCase(email),
        password = passwordFieldUseCase(password)
    )




}