package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository

import javax.inject.Inject

class DeleteProductFromCartUseCase  @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(productId: Int) =
        try {
            cartRepository.deleteProductFromCart(productId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
}