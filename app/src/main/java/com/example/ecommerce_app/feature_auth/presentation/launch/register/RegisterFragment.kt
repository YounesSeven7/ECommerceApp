package com.example.ecommerce_app.feature_auth.presentation.launch.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.databinding.FragmentRegisterBinding
import com.example.ecommerce_app.feature_auth.domain.util.RegisterFields
import com.example.ecommerce_app.feature_auth.presentation.utile.setErrorMessageInEditText
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.feature_auth.presentation.utile.startOrBackToLoginScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.registerFieldsValidation.collect{ handleRegisterFieldValidation(it) } }

            launch { viewModel.register.collect{ handleRegisterState(it) } }
        }
    }

    private fun FragmentRegisterBinding.makeScreenReady() {
        registerButtonOnClick()
        doYouHaveAccountTvOnClick()
    }

    private fun FragmentRegisterBinding.registerButtonOnClick() {
        btnRegister.setOnClickListener {
            val (user, password) = getRegisterData()
            lifecycleScope.launch { viewModel.register(user, password) }
        }
    }

    private fun FragmentRegisterBinding.getRegisterData(): Pair<User, String> {
        val user = User(
            firstName = edFirstName.text.toString().trim(),
            lastName = edLastName.text.toString().trim(),
            email = edEmail.text.toString().trim()
        )
        val password = edPassword.text.toString().trim()
        return Pair(user, password)
    }

    private fun FragmentRegisterBinding.doYouHaveAccountTvOnClick() {
        tvDoYouHaveAccount.setOnClickListener {
            startOrBackToLoginScreen(findNavController())
        }
    }

    private fun handleRegisterFieldValidation(registerFields: RegisterFields) {
        binding.apply {
            btnRegister.revertAnimation()
            if (!registerFields.firstName.validation)
                setErrorMessageInEditText(edFirstName, registerFields.firstName.message)

            else if (!registerFields.lastName.validation)
                setErrorMessageInEditText(edLastName, registerFields.lastName.message)

            else if (!registerFields.email.validation)
                setErrorMessageInEditText(edEmail, registerFields.email.message)

            else
                setErrorMessageInEditText(edPassword, registerFields.password.message)
        }

    }

    private fun handleRegisterState(registerState: Resource<Boolean>) {
        when(registerState) {
            is Resource.Loading -> binding.btnRegister.startAnimation()
            is Resource.Success -> handleSuccessRegisterState(registerState)
            is Resource.Error -> handleErrorRegisterState(registerState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessRegisterState(successRegisterState: Resource.Success<Boolean>) {
        binding.btnRegister.revertAnimation {
            startOrBackToLoginScreen(findNavController())
        }
    }

    private fun handleErrorRegisterState(errorRegisterState: Resource.Error<Boolean>) {
        binding.btnRegister.revertAnimation()
        showAlongSnackbar(requireView(), errorRegisterState.message)
    }


}