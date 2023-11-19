package com.example.ecommerce_app.core.domain.use_case.user

import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.repository.UserRepository

import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(user: User) = userRepository.addUser(user)
}