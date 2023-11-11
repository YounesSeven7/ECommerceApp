package com.example.ecommerce_app.feature_auth.presentation.launch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecommerce_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }
}

