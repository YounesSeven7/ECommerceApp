package com.example.ecommerce_app.feature_shopping.domain.use_case.address

import com.example.ecommerce_app.core.domain.use_case.fields.PhoneNumberFieldValidationUseCase
import com.example.ecommerce_app.core.domain.use_case.fields.SimpleFieldValidationUseCase
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.domain.util.AddressFields
import javax.inject.Inject

class AddressFieldsValidationUseCase @Inject constructor(
    private val addressLocationFieldValidationUseCase: SimpleFieldValidationUseCase,
    private val fullNameFieldValidationUseCase: SimpleFieldValidationUseCase,
    private val streetFieldValidationUseCase: SimpleFieldValidationUseCase,
    private val phoneNumberFieldValidationUseCase: PhoneNumberFieldValidationUseCase,
    private val cityNameFieldValidationUseCase: SimpleFieldValidationUseCase,
    private val stateNameFieldValidationUseCase: SimpleFieldValidationUseCase
) {
    operator fun invoke(address: Address) =
        AddressFields(
            addressLocation = addressLocationFieldValidationUseCase(address.addressTitle),
            fullName = fullNameFieldValidationUseCase(address.fullName),
            street = streetFieldValidationUseCase(address.street),
            phoneNumber = phoneNumberFieldValidationUseCase(address.phone),
            cityName = cityNameFieldValidationUseCase(address.city),
            stateName= stateNameFieldValidationUseCase(address.state)
        )

}