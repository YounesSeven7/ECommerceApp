package com.example.ecommerce_app.feature_auth.presentation.launch.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.databinding.FragmentSplashScreenBinding
import com.example.ecommerce_app.feature_shopping.presentation.shopping.ShoppingActivity
import com.example.ecommerce_app.feature_auth.presentation.utile.startShoppingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment: Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private val viewModel by viewModels<SplashViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.checkStartingPreferences().collect { startingPreferences ->
                delay(1000)
                if (startingPreferences.isUserLogin) startShoppingActivity(requireActivity())
                else if (startingPreferences.isStartBtnClicked) navigateToAccountOptionsFragment()
                else navigateToIntroductionFragment()
            }
        }
    }


    private fun navigateToAccountOptionsFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_accountOptionsFragment)
    }

    private fun navigateToIntroductionFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_introductionFragment)
    }




}