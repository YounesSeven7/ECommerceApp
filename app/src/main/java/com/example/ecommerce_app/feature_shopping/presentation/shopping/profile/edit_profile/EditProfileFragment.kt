package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.edit_profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.databinding.FragmentEditProfileBinding
import com.example.ecommerce_app.databinding.FragmentProfileBinding
import com.example.ecommerce_app.feature_auth.presentation.utile.setErrorMessageOnEditText
import com.example.ecommerce_app.feature_shopping.domain.util.EditProfileFields
import com.example.ecommerce_app.feature_shopping.domain.util.getImageRequest
import com.example.ecommerce_app.feature_shopping.presentation.hideBottomNavigation
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment: Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel>()
    private val args by navArgs<EditProfileFragmentArgs>()
    @Inject lateinit var imageLoader: ImageLoader

    private lateinit var imageActivityResult: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val imageUrl = it.data?.data
                imageUrl?.let {
                    viewModel.imageUrl = imageUrl.toString()
                    binding.apply { setImage(imageUrl.toString()) }
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady(args.user) }

        lifecycleScope.apply {

            launch { viewModel.updateUserState.collect{ handleUpdateUserState(it) } }

            launch { viewModel.editProfileFieldsValidation.collect{ handleEditProfileFieldsValidation(it) } }
        }
    }

    private fun FragmentEditProfileBinding.makeScreenReady(user: User) {
        hideBottomNavigation()
        user.apply {
            viewModel.imageUrl = imageUrl
            if (imageUrl.isNotEmpty()) setImage(imageUrl)
            edFirstName.setText(firstName)
            edLastName.setText(lastName)
            edEmail.setText(email)
            setupButtons()
        }
    }

    private fun FragmentEditProfileBinding.setImage(imageUrl: String) {
        val request = getImageRequest(imgUser, imageUrl)
        imageLoader.enqueue(request)
    }

    private fun FragmentEditProfileBinding.setupButtons() {
        btnSaveProfile.setOnClickListener {
            lifecycleScope.launch { viewModel.updateUserData(args.user, getUserDate()) }
        }
        imgEdit.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                imageActivityResult.launch(this)
            }
        }
    }

    private fun FragmentEditProfileBinding.getUserDate() = User(
        firstName = edFirstName.text.toString().trim(),
        lastName = edLastName.text.toString().trim(),
        email = args.user.email,
        imageUrl = viewModel.imageUrl
    )



    private fun handleUpdateUserState(updateUserState: Resource<String>) {
        when(updateUserState) {
            is Resource.Loading -> binding.btnSaveProfile.startAnimation()
            is Resource.Success -> handleSuccessUpdateUserState(updateUserState.data)
            is Resource.Error -> handleErrorUpdateUserState(updateUserState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessUpdateUserState(updatingMessage: String) {
        binding.btnSaveProfile.revertAnimation{
            showAlongSnackbar(requireView(), updatingMessage)
            findNavController().popBackStack()
        }
    }

    private fun handleErrorUpdateUserState(message: String) {
        Log.d("younes", "$message")
        binding.btnSaveProfile.revertAnimation {
            showAlongSnackbar(requireView(), message)
        }
    }


    private fun handleEditProfileFieldsValidation(editProfileFields: EditProfileFields) {
        binding.apply {
            if (!editProfileFields.firstName.validation)
                setErrorMessageOnEditText(edFirstName, editProfileFields.firstName.message)
            else if (!editProfileFields.lastName.validation)
                setErrorMessageOnEditText(edLastName, editProfileFields.lastName.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showBottomNavigation()
    }


}