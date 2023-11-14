package com.example.ecommerce_app.feature_auth.presentation.launch.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Field
import com.example.ecommerce_app.databinding.FragmentResetPasswordBinding
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.feature_auth.presentation.utile.setErrorMessageOnEditText
import com.example.ecommerce_app.core.presentation.showAShortToast
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment: Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel by viewModels<ResetPasswordViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.emailFieldValidation.collect{ handleEmailFieldValidation(it) } }

            launch { viewModel.resetPasswordState.collect { handleResetPasswordState(it) } }

        }
    }

    private fun FragmentResetPasswordBinding.makeScreenReady() {
        btnResetPassword.setOnClickListener {
            val email = edEmailReset.text.trim().toString()
            lifecycleScope.launch { viewModel.resetPassword(email) }
        }
    }

    private fun handleEmailFieldValidation(emailField: Field) {
        binding.btnResetPassword.revertAnimation()
        setErrorMessageOnEditText(binding.edEmailReset, emailField.message)
    }

    private fun handleResetPasswordState(resetPasswordState: Resource<Unit>) {
        when(resetPasswordState) {
            is Resource.Loading -> binding.btnResetPassword.startAnimation()
            is Resource.Success -> handleSuccessResetPasswordState(resetPasswordState)
            is Resource.Error -> handleErrorResetPasswordState(resetPasswordState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessResetPasswordState(resetPasswordState: Resource.Success<Unit>) {
        binding.btnResetPassword.revertAnimation {
            showAShortToast(requireContext(), getString(R.string.g_reset_password_message))
            findNavController().popBackStack()
        }

    }

    private fun handleErrorResetPasswordState(resetPasswordState: Resource.Error<Unit>) {
        binding.btnResetPassword.revertAnimation()
        showAlongSnackbar(requireView(), resetPasswordState.message)
    }






}