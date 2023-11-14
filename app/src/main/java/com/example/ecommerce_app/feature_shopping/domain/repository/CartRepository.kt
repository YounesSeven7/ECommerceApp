package com.example.ecommerce_app.feature_shopping.domain.repository

import com.example.ecommerce_app.core.domain.use_case.user.GetUserDocumentUseCase
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserDocumentUseCase: GetUserDocumentUseCase
){
     fun getCartCollection() = getUserDocumentUseCase().collection(Constant.CARD_COLLECTION)

    fun getCartProductDocument(productId: Int) = getCartCollection().document("$productId")

    suspend fun getCartProduct(productId: Int) = getCartProductDocument(productId)
        .get().await().toObject(CartProduct::class.java)

    suspend fun getCartProductsList() = getCartCollection()
        .get().await().map { it.toObject(CartProduct::class.java) }

    fun listenToData(listen: (List<CartProduct>, String?) -> Unit) {
        getCartCollection().addSnapshotListener { value, e ->
            val list = value?.documents?.map { it.toObject(CartProduct::class.java)!! } ?: emptyList()

            val message = e?.message

            listen(list, message)
        }
    }

    suspend fun checkProductInCart(productId: Int): Boolean  = getCartProductDocument(productId)
        .get().await().exists()

    fun addProductToCard(product: Product) {
        getCartProductDocument(product.id).set(CartProduct(product, 1))
    }

    fun increaseProductQuantityByOne(cartProduct: CartProduct) {
        getCartProductDocument(cartProduct.product.id)
            .update("quantity", cartProduct.quantity + 1)
    }

    fun updateCartProducts(productToUpdate: Map<CartProduct, Int>) {
        val batch = db.batch()
        for ((cartProduct, newQuantity) in productToUpdate) {
            val document = getCartProductDocument(cartProduct.product.id)
            val updates = mapOf("quantity" to newQuantity)
            batch.update(document, updates)
        }
        batch.commit()
    }

    fun deleteListOfProductsFromCart(cartProductsList: List<CartProduct>) {
        db.runBatch {
            for (product in cartProductsList) deleteProductFromCart(product.product.id)
        }
    }


    fun deleteProductFromCart(productId: Int) {
        getCartProductDocument(productId).delete()
    }






}