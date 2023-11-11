package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import javax.inject.Inject

class IsProductsExistsInCart @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product) =
        try {
            val existing = cartRepository.checkProductInCart(product.id)
            Resource.Success(existing)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

}