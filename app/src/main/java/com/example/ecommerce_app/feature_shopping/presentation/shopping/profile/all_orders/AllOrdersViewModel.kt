package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.all_orders

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.example.ecommerce_app.feature_shopping.domain.use_case.order.ListenToUserOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val listenToUserOrderUseCase: ListenToUserOrdersUseCase
): ViewModel() {

    private val _getAllOrdersState = MutableStateFlow<Resource<List<Order>>>(Resource.Loading())
    val getAllOrdersState: StateFlow<Resource<List<Order>>> = _getAllOrdersState

    fun listenToOrders() {
        listenToUserOrderUseCase{ result ->
            _getAllOrdersState.value = result
        }
    }

}
