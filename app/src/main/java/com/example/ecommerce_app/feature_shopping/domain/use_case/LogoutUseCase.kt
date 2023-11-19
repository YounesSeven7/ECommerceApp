package com.example.ecommerce_app.feature_shopping.domain.use_case

import com.example.ecommerce_app.core.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke() = authRepository.logout()
}