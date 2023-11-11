package com.example.ecommerce_app.core.domain.use_case.user

import com.example.ecommerce_app.core.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDocumentUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.getUserDocument()
}