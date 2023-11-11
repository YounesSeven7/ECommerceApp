package com.example.ecommerce_app.core.domain.use_case.fields

import android.app.Application
import javax.inject.Inject
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.core.domain.util.firstNameValidation

class FirstNameFieldUseCase @Inject constructor(
    private val context: Application
) {
    operator fun invoke(firstName: String): Field = context.firstNameValidation(firstName)
}