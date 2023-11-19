package com.example.ecommerce_app.feature_shopping.domain.use_case.edit_profile

import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.use_case.fields.FirstNameFieldUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.LastNameFieldUseCase
import com.example.ecommerce_app.feature_shopping.domain.util.EditProfileFields
import javax.inject.Inject

class EditProfileFieldsValidationUseCase @Inject constructor(
    private val firstNameFieldUseCase: FirstNameFieldUseCase,
    private val lastNameFieldUseCase: LastNameFieldUseCase
) {
    operator fun invoke(user: User) = EditProfileFields(
        firstName = firstNameFieldUseCase(user.firstName),
        lastName = lastNameFieldUseCase(user.lastName)
    )
}