package com.example.ecommerce_app.feature_shopping.domain.use_case.product

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.remote_data.ProductsApi
import com.example.ecommerce_app.feature_shopping.data.model.Product
import javax.inject.Inject

class GetProductsByNameUseCase @Inject constructor(
    private val productsApi: ProductsApi
) {
    suspend operator fun invoke(productName: String): Resource<List<Product>> {
        return try {
            val productsList = productsApi.getProductsByName(productName)
            Resource.Success(productsList)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}