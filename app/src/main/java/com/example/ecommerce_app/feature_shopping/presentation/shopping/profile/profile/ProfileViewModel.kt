package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.profile

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.use_case.user.GetUserUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserCase: GetUserUseCase
): ViewModel() {


    private val _getUserState = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val getUserState: StateFlow<Resource<User>> = _getUserState

    suspend fun getUser() {
        _getUserState.emit(Resource.Loading())
        val result = getUserCase()
        _getUserState.emit(result)
    }


}
