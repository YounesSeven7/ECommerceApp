package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageFragment: Fragment() {

    private lateinit var binding: FragmentLanguageBinding
    private val viewModel by viewModels<LanguageViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getCurrentLanguage().collect{ handleGetCurrentLanguage(it) } }
        }

    }

    private fun FragmentLanguageBinding.makeScreenReady() {

    }

    private fun handleGetCurrentLanguage(currentLanguage: String) {
        when(currentLanguage) {
            Constant.ENGLISH -> showThisView(binding.imgEnglish)
            Constant.ARABIC -> showThisView(binding.imgArabic)
        }
    }
}