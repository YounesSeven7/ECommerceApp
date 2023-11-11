package com.example.ecommerce_app.feature_auth.presentation.launch.login

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.feature_auth.domain.use_case.login.LoginFieldsUseCase
import com.example.ecommerce_app.feature_auth.domain.use_case.login.LoginUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_auth.domain.util.LoginFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginFieldsUseCase: LoginFieldsUseCase
): ViewModel() {
    
    private val _loginFieldsValidation = MutableSharedFlow<LoginFields>()
    val loginFieldsValidation = _loginFieldsValidation.asSharedFlow()

    private val _login = MutableSharedFlow<Resource<Unit>>()
    val login = _login.asSharedFlow()

    suspend fun login(email: String, password: String) {
        _login.emit(Resource.Loading())
        delay(500)

        val loginFields = loginFieldsUseCase(email, password)
        val validation = checkLoginValidation(loginFields)

        if (validation) {
            val result = loginUseCase(email, password)
            delay(500)
            _login.emit(result)
        } else {
            _loginFieldsValidation.emit(loginFields)
        }
    }

    private fun checkLoginValidation(loginFields: LoginFields): Boolean {
        return loginFields.email.validation &&
                loginFields.password.validation
    }
}