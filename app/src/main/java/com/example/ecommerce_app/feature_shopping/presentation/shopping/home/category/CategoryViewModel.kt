package com.example.ecommerce_app.feature_shopping.presentation.shopping.home.category

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.domain.use_case.product.GetProductsByCategoryIdAndOffsetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getProductsByCategoryIdAndOffsetUseCase: GetProductsByCategoryIdAndOffsetUseCase
): ViewModel() {

    private val _getProductsState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val getProductsState: StateFlow<Resource<List<Product>>> = _getProductsState

    var offset = 0
    val productsList = mutableListOf<Product>()

    suspend fun getProductsByCategoryId(categoryId: Int) {
        val result = getProductsByCategoryIdAndOffsetUseCase(categoryId, offset)
        if (result is Resource.Success) {
            productsList.addAll(result.data)
            _getProductsState.emit(Resource.Success(productsList))
        } else {
            _getProductsState.emit(result)
        }

    }


}
