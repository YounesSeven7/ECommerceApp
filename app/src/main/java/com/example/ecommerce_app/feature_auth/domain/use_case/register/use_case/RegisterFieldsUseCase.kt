package com.example.ecommerce_app.feature_auth.domain.use_case.register.use_case


import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.use_case.fields.EmailFieldValidationUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.FirstNameFieldUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.LastNameFieldUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.PasswordFieldUseCase

import com.example.ecommerce_app.feature_auth.domain.util.RegisterFields

import javax.inject.Inject


class RegisterFieldsUseCase @Inject constructor(
    private val firstNameFieldUseCase: FirstNameFieldUseCase,
    private val lastNameFieldUseCase: LastNameFieldUseCase,
    private val emailFieldValidationUseCase: EmailFieldValidationUseCase,
    private val passwordFieldUseCase: PasswordFieldUseCase
) {
    operator fun invoke(user: User, password: String) =  RegisterFields(
        firstName = firstNameFieldUseCase(user.firstName),
        lastName = lastNameFieldUseCase(user.lastName),
        email = emailFieldValidationUseCase(user.email),
        password = passwordFieldUseCase(password)
    )




}