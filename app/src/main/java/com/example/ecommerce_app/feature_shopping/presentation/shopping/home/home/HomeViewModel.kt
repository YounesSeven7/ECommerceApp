package com.example.ecommerce_app.feature_shopping.presentation.shopping.home.home

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.data.model.Category
import com.example.ecommerce_app.feature_shopping.domain.use_case.product.GetAllCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
): ViewModel()  {

    private val _getAllCategoriesState = MutableStateFlow<Resource<List<Category>>>(Resource.Loading())
    val getAllCategoriesState: StateFlow<Resource<List<Category>>> = _getAllCategoriesState

    var doWeHaveCategories = false

    suspend fun getAllCategories() {
        val result = getAllCategoriesUseCase()
        if (result != _getAllCategoriesState.value) {
            _getAllCategoriesState.emit(Resource.Loading())
            delay(2000)
        }
        _getAllCategoriesState.emit(result)
    }
}