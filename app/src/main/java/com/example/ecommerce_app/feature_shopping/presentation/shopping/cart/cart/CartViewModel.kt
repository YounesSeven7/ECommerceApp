package com.example.ecommerce_app.feature_shopping.presentation.shopping.cart.cart

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.data.model.Payment
import com.example.ecommerce_app.feature_shopping.domain.use_case.cart.ListenToCartProductsUseCase
import com.example.ecommerce_app.feature_shopping.domain.use_case.cart.UpdateCartProductQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

import javax.inject.Inject

typealias newQuantity = Int
typealias price = Int

@HiltViewModel
class CartViewModel @Inject constructor(
    private val listenToCartProductsUseCase: ListenToCartProductsUseCase,
    private val updateCartProductQuantityUseCase: UpdateCartProductQuantityUseCase
): ViewModel() {

    private val cartProductsNewQuantityToUpdate = mutableMapOf<CartProduct, newQuantity>()

    private val _getCartProductsState = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Loading())
    val getCartProductsState: StateFlow<Resource<List<CartProduct>>> = _getCartProductsState

    private val _updateCartProductsState = MutableSharedFlow<Resource<String>>()
    val updateCartProductsState = _updateCartProductsState.asSharedFlow()

    private val _currentPrice = MutableStateFlow(0)
    val currentPrice: StateFlow<Int> = _currentPrice

    private val _doWeHaveChanges = MutableStateFlow(false)
    val doWeHaveChanges: StateFlow<Boolean> = _doWeHaveChanges

    private val _showDeleteItemDialogState = MutableSharedFlow<CartProduct>()
    val showDeleteItemDialogState = _showDeleteItemDialogState.asSharedFlow()

    private val _deleteItemState = MutableSharedFlow<Resource<Unit>>()
    val deleteItemState = _deleteItemState.asSharedFlow()


    fun listenToCartProductState() {
        listenToCartProductsUseCase { result ->
            _getCartProductsState.value = result
            if (result is Resource.Success)
                _currentPrice.value = calculatePrice(result.data)
        }
    }

    private fun calculatePrice(cartProductList: List<CartProduct>) = cartProductList
        .sumOf { it.product.price * getNewQuantity(it) }

    suspend fun updateProductsQuantity() {
        _updateCartProductsState.emit(Resource.Loading())
        val result = updateCartProductQuantityUseCase(cartProductsNewQuantityToUpdate)
        if (result is Resource.Success) {
            cartProductsNewQuantityToUpdate.clear()
            _doWeHaveChanges.emit(false)
        }
        _updateCartProductsState.emit(result)

    }

    fun checkout(): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading())
        val result = when(val cartProductsState = getCartProductsState.value) {
            is Resource.Success -> Resource.Success(Payment(cartProductsState.data, currentPrice.value))
            else -> Resource.Error("Something went wrong")
        }
        delay(1000)
        emit(result)
    }


    fun deleteProducts(cartProduct: CartProduct) {
        // todo delete item
    }

    fun increaseOrDecreaseQuantity(cartProduct: CartProduct, increase: Boolean) {
        val price = cartProduct.product.price
        val quantityToUpdate = getNewQuantity(cartProduct)

        if (quantityToUpdate == 1 && !increase) {
            runBlocking { _showDeleteItemDialogState.emit(cartProduct) }
            return
        }

        _currentPrice.value += if (increase) price else -price

        cartProductsNewQuantityToUpdate[cartProduct] = (quantityToUpdate) + (if (increase) +1 else -1)

        if (cartProduct.quantity == cartProductsNewQuantityToUpdate[cartProduct])
            cartProductsNewQuantityToUpdate.remove(cartProduct)

        _doWeHaveChanges.value = cartProductsNewQuantityToUpdate.isNotEmpty()
    }

    fun getNewQuantity(cartProduct: CartProduct): Int {
        return cartProductsNewQuantityToUpdate[cartProduct] ?: cartProduct.quantity
    }

}
