package com.example.ecommerce_app.feature_shopping.domain.use_case.edit_profile

import android.app.Application
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.repository.UserRepository
import com.example.ecommerce_app.core.domain.use_case.CheckInternetConnectionUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    private val context: Application
) {
    suspend operator fun invoke(oldUser: User, newUser: User): Resource<String> {
        return try {
            if (oldUser == newUser) return Resource.Error(context.getString(R.string.There_is_no_changes))

            userRepository.updateUser(oldUser, newUser)
            val connection = checkInternetConnectionUseCase()
            if (connection) Resource.Success(context.getString(R.string.update_successfully))
            else Resource.Success(context.getString(R.string.save_changes_no_internet))

        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

}