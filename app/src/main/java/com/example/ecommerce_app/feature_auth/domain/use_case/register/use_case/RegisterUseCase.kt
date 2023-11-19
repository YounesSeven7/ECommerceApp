package com.example.ecommerce_app.feature_auth.domain.use_case.register.use_case

import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.repository.AuthRepository
import com.example.ecommerce_app.core.domain.use_case.user.AddUserUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val addUserUseCase: AddUserUseCase
) {

    suspend operator fun invoke(user: User, password: String): Resource<Boolean> {
        return try {
            authRepository.register(user, password)
            addUserUseCase.invoke(user)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

}