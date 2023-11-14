package com.example.ecommerce_app.core.domain.use_case.fields

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Field
import javax.inject.Inject

data class SimpleFieldValidationUseCase @Inject constructor(
    private val context: Application
) {
    operator fun invoke(text: String): Field =
        if (text.isEmpty())
            Field(message = context.getString(R.string.empty_field))
        else
            Field(validation = true)

}
