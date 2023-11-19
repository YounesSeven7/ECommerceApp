package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.edit_profile

import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.feature_shopping.domain.use_case.edit_profile.UpdateUserUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_shopping.domain.use_case.edit_profile.EditProfileFieldsValidationUseCase
import com.example.ecommerce_app.feature_shopping.domain.util.EditProfileFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val editProfileFieldsValidationUseCase: EditProfileFieldsValidationUseCase
): ViewModel() {

    var imageUrl = ""

    private val _updateUserState = MutableSharedFlow<Resource<String>>()
    val updateUserState = _updateUserState.asSharedFlow()

    private val _editProfileFieldsValidation = MutableSharedFlow<EditProfileFields>()
    val editProfileFieldsValidation = _editProfileFieldsValidation.asSharedFlow()

    suspend fun updateUserData(oldUser: User, newUser: User) {
        _updateUserState.emit(Resource.Loading())
        val editProfileFields = editProfileFieldsValidationUseCase(newUser)
        val validation = checkEditProfileFieldsValidation(editProfileFields)
        if (validation) {
            val result = updateUserUseCase(oldUser, newUser)
            _updateUserState.emit(result)
        } else
            _editProfileFieldsValidation.emit(editProfileFields)

    }

    private fun checkEditProfileFieldsValidation(editProfileFields: EditProfileFields): Boolean {
        return editProfileFields.firstName.validation &&
                editProfileFields.lastName.validation
    }

}
