package com.example.ecommerce_app.feature_auth.domain.use_case.login

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.repository.AuthRepository
import com.example.ecommerce_app.core.domain.use_case.user.IsUserExistUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val isUserExistUseCase: IsUserExistUseCase,
    private val context: Application
) {

    suspend operator fun invoke(email: String, password: String): Resource<Unit> {
        return try {

            if (!isUserExistUseCase(email))
                return Resource.Error(context.getString(R.string.user_not_exist))

            val firebaseUser = authRepository.login(email, password)!!

            if (firebaseUser.isEmailVerified)
                Resource.Success(Unit)
            else {
                authRepository.logout()
                Resource.Error(context.getString(R.string.verify_your_email))
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }


}