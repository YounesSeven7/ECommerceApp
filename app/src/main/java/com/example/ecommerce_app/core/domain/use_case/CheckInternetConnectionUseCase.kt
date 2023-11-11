package com.example.ecommerce_app.core.domain.use_case

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class CheckInternetConnectionUseCase @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    operator fun invoke(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val connection = capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P) ||
                    hasCapability(NetworkCapabilities.NET_CAPABILITY_FOTA)
        } ?: false
        return connection
    }
}