package com.example.ecommerce_app.core.domain.use_case.fields

import android.app.Application
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.core.domain.util.passwordValidation
import javax.inject.Inject

class PasswordFieldUseCase @Inject constructor(
    private val context: Application
) {
    operator fun invoke(password: String): Field = context.passwordValidation(password)
}