package com.example.ecommerce_app.core.domain.use_case.user

import com.example.ecommerce_app.core.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class IsUserExistUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String) = userRepository.isUserExist(email)

}