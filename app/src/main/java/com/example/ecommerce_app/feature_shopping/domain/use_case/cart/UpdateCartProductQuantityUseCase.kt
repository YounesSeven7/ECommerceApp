package com.example.ecommerce_app.feature_shopping.domain.use_case.cart

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.use_case.CheckInternetConnectionUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.domain.repository.CartRepository
import com.example.ecommerce_app.feature_shopping.presentation.shopping.cart.cart.newQuantity
import javax.inject.Inject

class UpdateCartProductQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    private val context: Application
){
    operator fun invoke(cartProductsNewQuantityToUpdate: Map<CartProduct, newQuantity>): Resource<String> {
        return try {
            cartRepository.updateCartProducts(cartProductsNewQuantityToUpdate)
            val isConnected = checkInternetConnectionUseCase()
            if (isConnected) Resource.Success(context.getString(R.string.update_successfully))
            else Resource.Success(context.getString(R.string.save_changes_no_internet))
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}