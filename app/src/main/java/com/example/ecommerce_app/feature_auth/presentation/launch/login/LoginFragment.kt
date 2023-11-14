package com.example.ecommerce_app.feature_auth.presentation.launch.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.databinding.FragmentLoginBinding
import com.example.ecommerce_app.feature_auth.domain.util.LoginFields
import com.example.ecommerce_app.feature_auth.presentation.utile.setErrorMessageOnEditText
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.feature_auth.presentation.utile.startOrBAckToRegisterScreen
import com.example.ecommerce_app.feature_auth.presentation.utile.startShoppingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.loginFieldsValidation.collect{ handleLoginFieldsValidation(it) } }

            launch { viewModel.login.collect { handleLoginState(it) } }
        }
    }

    private fun FragmentLoginBinding.makeScreenReady() {
        loginBtnOnClick()
        doNotHaveAnAccountTvOnClick()
        forgetPasswordTvOnClick()
    }

    private fun FragmentLoginBinding.forgetPasswordTvOnClick() {
        tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    private fun FragmentLoginBinding.loginBtnOnClick() {
        btnLogin.setOnClickListener {
            val (email, password) = getLoginData()
            lifecycleScope.launch { viewModel.login(email, password) }
        }
    }

    private fun FragmentLoginBinding.getLoginData(): Pair<String, String> {
        return Pair(
            edEmailLogin.text.toString().trim(),
            edPasswordLogin.text.toString().trim()
        )
    }

    private fun FragmentLoginBinding.doNotHaveAnAccountTvOnClick() {
        tvDoNotHaveAnAccount.setOnClickListener {
            startOrBAckToRegisterScreen(findNavController())
        }
    }


    private fun handleLoginFieldsValidation(loginFields: LoginFields) {
        binding.apply {
            btnLogin.revertAnimation()
            if (!loginFields.email.validation)
                setErrorMessageOnEditText(edEmailLogin , loginFields.email.message)

            else
                setErrorMessageOnEditText(edPasswordLogin, loginFields.password.message)
        }
    }

    private fun handleLoginState(loginState: Resource<Unit>) {
        when(loginState) {
            is Resource.Loading -> binding.btnLogin.startAnimation()
            is Resource.Success -> handleSuccessLoginState(loginState)
            is Resource.Error -> handleErrorLoginState(loginState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessLoginState(successLoginState: Resource.Success<Unit>) {
        binding.btnLogin.revertAnimation {
            startShoppingActivity(requireActivity())
        }
    }

    private fun handleErrorLoginState(errorLoginState: Resource.Error<Unit>) {
        binding.btnLogin.revertAnimation {
            showAlongSnackbar(requireView(), errorLoginState.message)
        }

    }


}