package com.example.ecommerce_app.core.domain.use_case.fields

import android.app.Application
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.core.domain.util.emailValidation
import javax.inject.Inject

class EmailFieldValidationUseCase @Inject constructor(
    private val context: Application
) {
    operator fun invoke(email: String): Field = context.emailValidation(email)
}