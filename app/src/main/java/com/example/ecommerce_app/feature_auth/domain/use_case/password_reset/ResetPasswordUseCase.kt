package com.example.ecommerce_app.feature_auth.domain.use_case.password_reset

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.repository.AuthRepository
import com.example.ecommerce_app.core.domain.use_case.user.IsUserExistUseCase
import javax.inject.Inject
import com.example.ecommerce_app.core.domain.util.Resource


class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val isUserExistUseCase: IsUserExistUseCase,
    private val context: Application
) {

    suspend operator fun invoke(email: String): Resource<Unit> {
        return try {
            if (isUserExistUseCase(email)) {
                authRepository.resetPassword(email)
                Resource.Success(Unit)
            } else
                Resource.Error(context.getString(R.string.user_not_exist))
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}