package com.example.ecommerce_app.feature_shopping.domain.use_case.order

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.example.ecommerce_app.feature_shopping.domain.repository.OrderRepository

import javax.inject.Inject

class ListenToUserOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    operator fun invoke(result: (Resource<List<Order>>) -> Unit) {
        orderRepository.listenToUserOrders { ordersList, error ->
            if (error == null) result(Resource.Success(ordersList))
            else result(Resource.Error(error))
        }
    }
}