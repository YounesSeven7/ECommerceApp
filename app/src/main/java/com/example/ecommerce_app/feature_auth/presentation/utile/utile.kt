package com.example.ecommerce_app.feature_auth.presentation.utile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.feature_shopping.presentation.shopping.ShoppingActivity
import com.google.android.material.snackbar.Snackbar



fun setErrorMessageInEditText(editText: EditText, message: String) {
    editText.apply {
        requestFocus()
        error = message
    }
}

@SuppressLint("RestrictedApi")
fun previousFragmentName(navigationController: NavController): String {
    val list = navigationController.currentBackStack.value
    return list[list.lastIndex - 1].destination.label.toString()
}


fun startOrBackToLoginScreen(navigationController: NavController) {
    if (previousFragmentName(navigationController) == Constant.LOGIN_FRAGMENT)
        navigationController.popBackStack()
    else
        navigationController.navigate(R.id.action_registerFragment_to_loginFragment)
}


fun startOrBAckToRegisterScreen(navigationController: NavController) {
    if (previousFragmentName(navigationController) == Constant.REGISTER_FRAGMENT)
        navigationController.popBackStack()
    else
        navigationController.navigate(R.id.action_loginFragment_to_registerFragment)
}


fun startShoppingActivity(requireActivity: FragmentActivity) {
    Intent(requireActivity, ShoppingActivity::class.java).also { intent ->
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity.startActivity(intent)
    }
}