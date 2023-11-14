package com.example.ecommerce_app.core.domain.use_case.user

import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.repository.UserRepository
import com.example.ecommerce_app.core.domain.util.Resource
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<User> =
        try {
            val user = userRepository.getUser()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

}