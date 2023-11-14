package com.example.ecommerce_app.feature_shopping.domain.use_case.order

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.use_case.CheckInternetConnectionUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import com.example.ecommerce_app.feature_shopping.domain.repository.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val db: FirebaseFirestore,
    private val context: Application,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase
) {
    operator fun invoke(order: Order): Resource<String> {
        val connection = checkInternetConnectionUseCase()
        if (!connection)
            return Resource.Success(context.getString(R.string.you_need_internet_connection_to_place_order))

        return try {
            db.runBatch {
                orderRepository.addOrder(order)
                cartRepository.deleteListOfProductsFromCart(order.cartProductList)
            }
            Resource.Success(context.getString(R.string.add_order_successfully))
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}