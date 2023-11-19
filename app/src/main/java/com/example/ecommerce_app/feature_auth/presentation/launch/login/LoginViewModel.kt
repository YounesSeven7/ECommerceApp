package com.example.ecommerce_app.feature_auth.presentation.launch.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.feature_auth.domain.use_case.login.login.LoginFieldsUseCase
import com.example.ecommerce_app.feature_auth.domain.use_case.login.login.LoginUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_auth.domain.use_case.login.login_google_login.GoogleSignInUsingIntentUseCase
import com.example.ecommerce_app.feature_auth.domain.use_case.login.login_google_login.GetGoogleSigInIntentSenderUseCase
import com.example.ecommerce_app.feature_auth.domain.util.LoginFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow


import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginFieldsUseCase: LoginFieldsUseCase,
    private val getGoogleSigInIntentSenderUseCase: GetGoogleSigInIntentSenderUseCase,
    private val googleSignInUsingIntentUseCase: GoogleSignInUsingIntentUseCase
): ViewModel() {
    
    private val _loginFieldsValidation = MutableSharedFlow<LoginFields>()
    val loginFieldsValidation = _loginFieldsValidation.asSharedFlow()

    private val _login = MutableSharedFlow<Resource<Unit>>()
    val login = _login.asSharedFlow()

    private val _googleLogin = MutableSharedFlow<Resource<Unit>>()
    val googleLogin = _googleLogin.asSharedFlow()

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

    suspend fun getGoogleSignInIntentSender() = flow {
        val intentSender = getGoogleSigInIntentSenderUseCase()
        emit(intentSender)
    }

    suspend fun googleSignInUsingIntent(intent: Intent) {
        _login.emit(Resource.Loading())
        val result = googleSignInUsingIntentUseCase(intent)
        _login.emit(result)
    }
}