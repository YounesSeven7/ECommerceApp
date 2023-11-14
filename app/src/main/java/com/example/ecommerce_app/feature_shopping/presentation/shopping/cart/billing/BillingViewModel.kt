package com.example.ecommerce_app.feature_shopping.presentation.shopping.cart.billing

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.example.ecommerce_app.feature_shopping.domain.use_case.address.GetAllAddressesUseCase
import com.example.ecommerce_app.feature_shopping.domain.use_case.order.AddOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val addOrderUseCase: AddOrderUseCase
): ViewModel() {



    private val _getAddressesState = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val getAddressesState: StateFlow<Resource<List<Address>>> = _getAddressesState

    private val _placeOrderState = MutableSharedFlow<Resource<String>>()
    val placeOrderState = _placeOrderState.asSharedFlow()

    private val _currentSelectedAddressPosition = MutableStateFlow(-1)
    val currentSelectedAddressPosition: StateFlow<Int> = _currentSelectedAddressPosition


    suspend fun getAllAddresses() {
        _getAddressesState.emit(Resource.Loading())
        val result = getAllAddressesUseCase()
        _getAddressesState.emit(result)
    }

    fun placeOrder(order: Order): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val result = addOrderUseCase(order)
        emit(result)
    }



    fun onChangeAddressPosition(newPosition: Int): Int {
        val oldPosition = currentSelectedAddressPosition.value
        _currentSelectedAddressPosition.value = newPosition
        return oldPosition
    }

}
