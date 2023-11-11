package com.example.ecommerce_app.feature_shopping.presentation

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecommerce_app.R
import com.example.ecommerce_app.feature_shopping.presentation.shopping.ShoppingActivity

fun makePriceWithFormat(context: Context, price: Int): String {
    return String.format(context.getString(R.string.price_format), price)
}

fun Fragment.getBottomNavigationView() = (activity as ShoppingActivity).binding.bottomNavigation


fun Fragment.showBottomNavigation() {
    getBottomNavigationView().visibility = View.VISIBLE
}

fun Fragment.hideBottomNavigation() {
    getBottomNavigationView().visibility = View.GONE
}