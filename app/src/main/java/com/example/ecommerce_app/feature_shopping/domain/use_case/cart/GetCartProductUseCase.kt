package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import javax.inject.Inject

class GetCartProductUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Int): Resource<CartProduct?> {
        return try {
            val cartProduct = cartRepository.getCartProduct(productId)
            Resource.Success(cartProduct)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}