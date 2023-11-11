package com.example.ecommerce_app.feature_auth.presentation.launch.resetPassword


import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.domain.use_case.fields.EmailFieldUseCase
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_auth.domain.use_case.password_reset.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val emailFieldUseCase: EmailFieldUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {

    private val _emailFieldValidation = MutableSharedFlow<Field>()
    val emailFieldValidation = _emailFieldValidation.asSharedFlow()

    private val _resetPasswordState = MutableSharedFlow<Resource<Unit>>()
    val resetPasswordState = _resetPasswordState.asSharedFlow()

    suspend fun resetPassword(email: String) {
        _resetPasswordState.emit(Resource.Loading())
        delay(500)

        val emailField = emailFieldUseCase(email)

        if (emailField.validation) {
            val result = resetPasswordUseCase(email)
            delay(500)
            _resetPasswordState.emit(result)
        } else {
            _emailFieldValidation.emit(emailField)
        }
    }


}