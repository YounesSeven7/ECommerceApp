package com.example.ecommerce_app.feature_shopping.domain.repository

import com.example.ecommerce_app.core.domain.use_case.user.GetUserDocumentUseCase
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias fieldName = String
typealias value = Any

class AddressRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserDocumentUseCase: GetUserDocumentUseCase
) {

    private fun getAddressCollection() = getUserDocumentUseCase().collection(Constant.ADDRESS_COLLECTION)

    private fun getAddressDocument(addressName: String) = getAddressCollection().document(addressName)

    fun addAddress(address: Address) = getAddressDocument(address.fullName).set(address)

    fun deleteAddress(address: Address) = getAddressDocument(address.fullName).delete()


    suspend fun getAllAddresses(): List<Address> {
        val task = getAddressCollection().get()
        val documents = task.await()
        return documents.map { it.toObject(Address::class.java) }
    }

    fun updateAddress(oldAddress: Address, newAddress: Address) {
        val updates = mutableMapOf<fieldName, value>()

        for (field in Address::class.java.declaredFields) {
            val oldValue = field.get(oldAddress)
            val newValue = field.get(newAddress)
            if (oldValue != newValue) updates[field.name] = newValue
        }

        getAddressDocument(oldAddress.fullName).set(updates)
    }

}