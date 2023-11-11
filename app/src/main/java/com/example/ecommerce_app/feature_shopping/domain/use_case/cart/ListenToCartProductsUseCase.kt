package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import com.example.ecommerce_app.core.domain.util.Resource
import javax.inject.Inject

class ListenToCartProductsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(result: (Resource<List<CartProduct>>) -> Unit) {
        cartRepository.listenToData() { cartProducts, error ->
            if (error == null) result(Resource.Success(cartProducts))
            else result(Resource.Error(error))
        }
    }
}