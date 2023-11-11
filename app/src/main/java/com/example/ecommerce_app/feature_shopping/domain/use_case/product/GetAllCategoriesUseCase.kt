package com.example.ecommerce_app.feature_shopping.domain.use_case.product

import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.ProductsApi
import com.example.ecommerce_app.feature_shopping.data.model.Category
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val productApi: ProductsApi
) {
    suspend operator fun invoke(): Resource<List<Category>> {
        return try {
            val list = productApi.getAllCategories()
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}