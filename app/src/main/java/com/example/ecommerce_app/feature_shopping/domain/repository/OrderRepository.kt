package com.example.ecommerce_app.feature_shopping.domain.repository

import com.example.ecommerce_app.core.domain.use_case.user.GetUserDocumentUseCase
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserDocumentUseCase: GetUserDocumentUseCase
) {

    fun getUserOrderCollection() = getUserDocumentUseCase().collection(Constant.ORDER_COLLECTION)

    fun getUserOrderDocument(orderId: String) = getUserOrderCollection().document(orderId)

    fun getStockOrderCollection() = db.collection(Constant.ORDER_COLLECTION)

    fun getStockOrderDocument(orderId: String) = getStockOrderCollection().document(orderId)

    fun addOrder(order: Order) {
        db.runBatch {
            getUserOrderDocument(order.id).set(order)
            getStockOrderDocument(order.id).set(order)
        }
    }

    fun listenToUserOrders(listen: (List<Order>, String?) -> Unit) {
        getUserOrderCollection().addSnapshotListener { value, error ->
            val list = value?.documents?.map { it.toObject(Order::class.java)!! } ?: emptyList()
            val message = error?.message
            listen(list, message)
        }
    }



}