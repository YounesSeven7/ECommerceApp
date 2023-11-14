package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import javax.inject.Inject

class IncreaseProductQuantityByOneUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val context: Application
) {
    operator fun invoke(cartProduct: CartProduct): Resource<String> {
        return try {
            cartRepository.increaseProductQuantityByOne(cartProduct)
            Resource.Success(context.getString(R.string.add_successfully))
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}