package com.example.ecommerce_app.feature_shopping.domain.use_case.address

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.use_case.CheckInternetConnectionUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.domain.repository.AddressRepository

import javax.inject.Inject

class AddAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    private val context: Application
) {
    operator fun invoke(address: Address): Resource<String> {
        return try {
            addressRepository.addAddress(address)
            val connection = checkInternetConnectionUseCase()
            if (connection) Resource.Success(context.getString(R.string.update_successfully))
            else Resource.Success(context.getString(R.string.save_changes_no_internet))
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}