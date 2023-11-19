package com.example.ecommerce_app.feature_shopping.domain.util

import android.widget.ImageView
import coil.request.ImageRequest

fun getImageRequest(targetImage: ImageView, url: String): ImageRequest {
    return ImageRequest.Builder(targetImage.context)
        .data(url)
        .crossfade(false)
        .target(targetImage)
        .build()
}

