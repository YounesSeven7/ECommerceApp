package com.example.ecommerce_app.feature_auth.domain.use_case.login.login

import com.example.ecommerce_app.core.domain.use_case.fields.EmailFieldValidationUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.PasswordFieldUseCase
import com.example.ecommerce_app.feature_auth.domain.util.LoginFields
import javax.inject.Inject

class LoginFieldsUseCase @Inject constructor(
    private val emailFieldValidationUseCase: EmailFieldValidationUseCase,
    private val passwordFieldUseCase: PasswordFieldUseCase
) {
    operator fun invoke(email: String, password: String) = LoginFields(
        email = emailFieldValidationUseCase(email),
        password = passwordFieldUseCase(password)
    )




}