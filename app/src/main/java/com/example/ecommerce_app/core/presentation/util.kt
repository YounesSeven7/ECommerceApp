package com.example.ecommerce_app.core.presentation

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun showAlongSnackbar(requireView: View, message: String) {
    Snackbar.make(requireView, message, Snackbar.LENGTH_LONG).show()
}


fun showAShortToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showThisView(view: View) {
    view.visibility = View.VISIBLE
}

fun hideThisView(view: View) {
    view.visibility = View.GONE
}