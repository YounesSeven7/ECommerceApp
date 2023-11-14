package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.address

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.domain.use_case.address.AddressFieldsValidationUseCase
import com.example.ecommerce_app.feature_shopping.domain.use_case.address.AddressOperationsUseCase
import com.example.ecommerce_app.feature_shopping.domain.util.AddressFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressOperationsUseCase: AddressOperationsUseCase,
    private val addressFieldsValidationUseCase: AddressFieldsValidationUseCase
): ViewModel() {

    private val _addAddressState = MutableSharedFlow<Resource<String>>()
    val addAddressState = _addAddressState.asSharedFlow()

    private val _updateAddressState = MutableSharedFlow<Resource<String>>()
    val updateAddressState = _updateAddressState.asSharedFlow()

    private val _deleteAddressState = MutableSharedFlow<Resource<String>>()
    val deleteAddressState = _deleteAddressState.asSharedFlow()

    private val _addressFieldsValidation = MutableSharedFlow<AddressFields>()
    val addressFieldsValidation = _addressFieldsValidation.asSharedFlow()


    suspend fun addAddress(address: Address) {
        val addressFields = addressFieldsValidationUseCase(address)
        val validation = checkAddressFieldsValidation(addressFields)
        if (validation) {
            _addAddressState.emit(Resource.Loading())
            val result = addressOperationsUseCase.addAddressUseCase(address)
            _addAddressState.emit(result)
        } else {
            _addressFieldsValidation.emit(addressFields)
        }
    }


    suspend fun updateAddress(oldAddress: Address, newAddress: Address) {
        val addressFields = addressFieldsValidationUseCase(newAddress)
        val validation = checkAddressFieldsValidation(addressFields)
        if (validation) {
            _updateAddressState.emit(Resource.Loading())
            val result = addressOperationsUseCase.updateAddressUseCase(oldAddress, newAddress)
            _updateAddressState.emit(result)
        } else {
            _addressFieldsValidation.emit(addressFields)
        }
    }

    suspend fun deleteAddress(address: Address) {
        _deleteAddressState.emit(Resource.Loading())
        val result = addressOperationsUseCase.deleteAddressUseCase(address)
        _deleteAddressState.emit(result)
    }

    // todo I will put checkAddressFieldsValidation in AddressFieldsValidationUseCase
    // todo  will return null if valid or addressField if not
    private fun checkAddressFieldsValidation(addressFields: AddressFields): Boolean {
        return  addressFields.addressLocation.validation &&
                addressFields.fullName.validation &&
                addressFields.street.validation &&
                addressFields.phoneNumber.validation &&
                addressFields.cityName.validation &&
                addressFields.stateName.validation
    }

}
