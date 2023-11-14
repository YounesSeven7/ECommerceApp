package com.example.ecommerce_app.core.domain.use_case.fields

import android.app.Application
import android.util.Patterns
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Field
import javax.inject.Inject

class PhoneNumberFieldValidationUseCase @Inject constructor(
    private val context: Application
) {
    operator fun invoke(text: String): Field =
        if (text.isEmpty())
            Field(message = context.getString(R.string.empty_field))
        else if (!Patterns.PHONE.matcher(text).matches())
            Field(message = context.getString(R.string.invalid_phone_format))
        else
            Field(validation = true)



}