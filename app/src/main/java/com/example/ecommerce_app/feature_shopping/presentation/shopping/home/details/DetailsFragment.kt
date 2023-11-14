package com.example.ecommerce_app.feature_shopping.presentation.shopping.home.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.databinding.FragmentDetailsBinding
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.presentation.adapters.viewPager.ImageViewPagerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment: Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<DetailsViewModel>()
    private val args by navArgs<DetailsFragmentArgs>()
    @Inject lateinit var imageLoader: ImageLoader


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { makeScreenReady(args.product) }

        lifecycleScope.apply {
            launch { viewModel.getCartProductState.collect { handleGetCartProductState(it) } }

            launch { viewModel.addOrUpdateProductsState.collect{ handleAddOrUpdateProductState(it) } }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentDetailsBinding.makeScreenReady(product: Product) {
        tvProductName.text = product.title
        tvProductPrice.text = "$${product.price}"
        tvProductDescription.text = product.description
        setViewPager(product.images)
        imgClose.setOnClickListener { findNavController().popBackStack() }
        btnAddToCart.setOnClickListener {
            lifecycleScope.launch { viewModel.getCartProduct(product.id) }
        }
    }

    private fun FragmentDetailsBinding.setViewPager(imagesList: List<String>) {
        val imageViewPagerAdapter = ImageViewPagerAdapter(imageLoader, imagesList)
        viewpager2Images.adapter = imageViewPagerAdapter
        circleIndicator.apply {
            attachTo(viewpager2Images)
        }
    }

    private fun handleGetCartProductState(cartProductState: Resource<CartProduct?>) {
        when(cartProductState) {
            is Resource.Loading -> binding.btnAddToCart.startAnimation()
            is Resource.Success -> handleSuccessGetCartProductState(cartProductState)
            is Resource.Error -> handleErrorGetCartProductState(cartProductState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessGetCartProductState(successCartProductState: Resource.Success<CartProduct?>) {
        binding.btnAddToCart.revertAnimation()
        if (successCartProductState.data == null) {
            lifecycleScope.launch { viewModel.addProduct(args.product) }
        } else {
            showAlertDialog(successCartProductState.data)
        }
    }

    private fun showAlertDialog(cartProduct: CartProduct) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_to_cart))
            .setMessage(getString(R.string.product_already_exists))
            .setPositiveButton(getString(R.string.increase)) { dialog, _ ->
                lifecycleScope.launch { viewModel.increaseProductQuantity(cartProduct) }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.g_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun handleErrorGetCartProductState(errorCartProductState: Resource.Error<CartProduct?>) {
        binding.btnAddToCart.revertAnimation()
        showAlongSnackbar(requireView(), errorCartProductState.message)
    }


    private fun handleAddOrUpdateProductState(addOrUpdateProductState: Resource<String>) {
        when(addOrUpdateProductState) {
            is Resource.Loading -> binding.btnAddToCart.startAnimation()
            is Resource.Success -> handleSuccessAddOrUpdateProductState(addOrUpdateProductState)
            is Resource.Error -> handleErrorAddOrUpdateProductState(addOrUpdateProductState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessAddOrUpdateProductState(successAddOrUpdateProductState: Resource.Success<String>) {
        binding.btnAddToCart.revertAnimation()
        showAlongSnackbar(requireView(), successAddOrUpdateProductState.data)
    }

    private fun handleErrorAddOrUpdateProductState(errorAddOrUpdateProductState: Resource.Error<String>) {
        binding.btnAddToCart.revertAnimation()
        showAlongSnackbar(requireView(), errorAddOrUpdateProductState.message)
    }










}