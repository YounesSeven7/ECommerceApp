package com.example.ecommerce_app.feature_shopping.presentation.shopping.search

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.domain.use_case.product.GetProductsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getProductsByNameUseCase: GetProductsByNameUseCase
): ViewModel() {

    private val _getProductByNameState = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val getProductByNameState: StateFlow<Resource<List<Product>>> = _getProductByNameState

    suspend fun getProductByName(productName: String) {
        _getProductByNameState.emit(Resource.Loading())
        val result = getProductsByNameUseCase(productName)
        _getProductByNameState.emit(result)
    }

}
