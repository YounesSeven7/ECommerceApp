package com.example.ecommerce_app.feature_auth.presentation.launch.register

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.feature_auth.domain.util.RegisterFields
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.feature_auth.domain.use_case.register.use_case.RegisterFieldsUseCase
import com.example.ecommerce_app.feature_auth.domain.use_case.register.use_case.RegisterUseCase
import com.example.ecommerce_app.core.domain.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerFieldsUseCase: RegisterFieldsUseCase,
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _registerFieldsValidation = MutableSharedFlow<RegisterFields>()
    val registerFieldsValidation = _registerFieldsValidation.asSharedFlow()

    private val _register = MutableSharedFlow<Resource<Boolean>>()
    val register = _register.asSharedFlow()

    suspend fun register(user: User, password: String) {
        _register.emit(Resource.Loading())
        delay(500)

        val registerFields = registerFieldsUseCase(user, password)
        val validation = checkRegisterFields(registerFields)

        if (validation) {
            val result = registerUseCase(user, password)
            delay(500)
            _register.emit(result)
        } else {
            _registerFieldsValidation.emit(registerFields)
        }
    }

    private fun checkRegisterFields(registerFields: RegisterFields): Boolean {
        return registerFields.firstName.validation &&
                registerFields.lastName.validation &&
                registerFields.email.validation &&
                registerFields.password.validation
    }


}
