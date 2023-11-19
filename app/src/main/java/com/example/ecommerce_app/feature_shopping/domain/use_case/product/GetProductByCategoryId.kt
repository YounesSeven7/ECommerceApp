package com.example.ecommerce_app.feature_shopping.domain.use_case.product

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.remote_data.ProductsApi
import com.example.ecommerce_app.feature_shopping.data.model.Product
import javax.inject.Inject

class GetProductByCategoryId @Inject constructor(
    private val productsApi: ProductsApi
) {
    suspend operator fun invoke(categoryId: Int): Resource<List<Product>> {
        return try {
            val list = productsApi.getProductsByCategoryId(categoryId)
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}