package com.example.ecommerce_app.feature_shopping.domain.repository

import com.example.ecommerce_app.core.domain.use_case.user.GetUserDocumentUseCase
import com.example.ecommerce_app.core.domain.util.Constant
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserDocumentUseCase: GetUserDocumentUseCase
) {

    private fun getAddressCollection() = getUserDocumentUseCase().collection(Constant.USER_COLLECTION)

    private fun getAddressDocument(addressName: String) = getAddressCollection().document(addressName)



}