package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import javax.inject.Inject

class GetCartCollectionUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke() = cartRepository.getCartCollection()
}



