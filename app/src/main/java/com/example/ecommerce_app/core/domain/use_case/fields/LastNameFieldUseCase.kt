package com.example.ecommerce_app.core.domain.use_case.fields

import android.app.Application
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.core.domain.util.lastNameValidation
import javax.inject.Inject

class LastNameFieldUseCase @Inject constructor(
    private val context: Application
) {
    operator fun invoke(lastName: String) : Field = context.lastNameValidation(lastName)
}