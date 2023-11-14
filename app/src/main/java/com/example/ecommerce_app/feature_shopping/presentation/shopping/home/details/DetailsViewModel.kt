package com.example.ecommerce_app.feature_shopping.presentation.shopping.home.details

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.domain.use_case.cart.IncreaseProductQuantityByOneUseCase
import com.example.ecommerce_app.feature_shopping.domain.use_case.cart.AddProductToCartUseCase
import com.example.ecommerce_app.feature_shopping.domain.use_case.cart.GetCartProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCartProductUseCase: GetCartProductUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val increaseProductQuantityByOneUseCase: IncreaseProductQuantityByOneUseCase
) : ViewModel() {

    private val _addOrUpdateProductsState = MutableSharedFlow<Resource<String>>()
    val addOrUpdateProductsState = _addOrUpdateProductsState.asSharedFlow()

    private val _getCartProductState = MutableSharedFlow<Resource<CartProduct?>>()
    val getCartProductState = _getCartProductState.asSharedFlow()

    suspend fun getCartProduct(productId: Int) {
        _getCartProductState.emit(Resource.Loading())
        val result = getCartProductUseCase(productId)
        _getCartProductState.emit(result)
    }

    suspend fun addProduct(product: Product) {
        _addOrUpdateProductsState.emit(Resource.Loading())
        val result = addProductToCartUseCase(product)
        _addOrUpdateProductsState.emit(result)
    }

    suspend fun increaseProductQuantity(cartProduct: CartProduct) {
        _addOrUpdateProductsState.emit(Resource.Loading())
        val result = increaseProductQuantityByOneUseCase(cartProduct)
        _addOrUpdateProductsState.emit(result)
    }

}
