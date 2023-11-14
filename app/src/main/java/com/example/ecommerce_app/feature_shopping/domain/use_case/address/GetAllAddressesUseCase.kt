package com.example.ecommerce_app.feature_shopping.domain.use_case.address


import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.domain.repository.AddressRepository
import javax.inject.Inject

class GetAllAddressesUseCase @Inject constructor(
    private val addressRepository: AddressRepository,
) {
    suspend operator fun invoke(): Resource<List<Address>> {
        return try {
            val addressesList = addressRepository.getAllAddresses()
            Resource.Success(addressesList)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}