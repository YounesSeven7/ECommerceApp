package com.example.ecommerce_app.feature_shopping.presentation.shopping.cart.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showAShortToast
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentCartBinding
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.data.model.Payment
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.CartProductAdapter
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment: Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<CartViewModel>()
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var cartProductAdapter: CartProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getCartProductsState.collect{ handleGetCartProductsState(it) } }

            launch { viewModel.listenToCartProductState() }

            launch { viewModel.currentPrice.collect{ setTotalPrice(it) } }

            launch { viewModel.doWeHaveChanges.collect{ changeBtnOperation(it) } }

            launch { viewModel.showDeleteItemDialogState.collect{ showAlertDialog(it) } }

            launch { viewModel.deleteItemState.collect{ handleDeleteItemState(it) } }

            launch { viewModel.updateCartProductsState.collect{ handleUpdateCartCartProductsState(it) } }

        }
    }


    private fun FragmentCartBinding.makeScreenReady() {
        showBottomNavigation()
        setupCartProductRv()
        setupBtn()
    }

    private fun FragmentCartBinding.setupCartProductRv() {
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartProductAdapter = CartProductAdapter(imageLoader, increaseOrDecreaseQuantity, getNewQuantity)
        rvCart.apply {
            layoutManager = linearLayoutManager
            adapter = cartProductAdapter
        }
    }

    private fun FragmentCartBinding.setupBtn() {
        btnCheckoutOrUpdate.setOnClickListener {
            if (viewModel.doWeHaveChanges.value) {
                lifecycleScope.launch { viewModel.updateProductsQuantity() }
            } else {
                lifecycleScope.launch {
                    viewModel.checkout().collect{ handleCheckOut(it) }
                }
            }
        }
    }

    private fun handleCheckOut(checkOutState: Resource<Payment>) {
        when(checkOutState) {
            is Resource.Loading -> binding.btnCheckoutOrUpdate.startAnimation()
            is Resource.Success -> handleSuccessCheckOutState(checkOutState.data)
            is Resource.Error -> Unit
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessCheckOutState(payment: Payment) {
        val code = CartFragmentDirections.actionCartFragmentToBillingFragment(payment)
        findNavController().navigate(code)
        binding.btnCheckoutOrUpdate.revertAnimation()
    }

    private val increaseOrDecreaseQuantity = { cartProduct: CartProduct, increase: Boolean ->
        viewModel.increaseOrDecreaseQuantity(cartProduct, increase)
    }

    private val getNewQuantity = { cartProduct: CartProduct -> viewModel.getNewQuantity(cartProduct) }



    private fun handleGetCartProductsState(getCartProductsState: Resource<List<CartProduct>>) {
        when(getCartProductsState) {
            is Resource.Loading -> handleLoadingGetCartProductsTate()
            is Resource.Success -> handleSuccessGetCartProductsState(getCartProductsState)
            is Resource.Error -> handleErrorGetCartProductsState(getCartProductsState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleLoadingGetCartProductsTate() {
        showThisView(binding.progressbarCart)
        binding.btnCheckoutOrUpdate.startAnimation()
    }

    private fun handleSuccessGetCartProductsState(cartProductsState: Resource.Success<List<CartProduct>>) {
        val list = cartProductsState.data
        binding.layoutCarEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        binding.totalBoxContainer.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
        binding.progressbarCart.visibility = View.GONE
        binding.btnCheckoutOrUpdate.revertAnimation()
        cartProductAdapter.diff.submitList(list)
    }

    private fun handleErrorGetCartProductsState(cartProductsState: Resource.Error<List<CartProduct>>) {
        hideThisView(binding.progressbarCart)
        showAlongSnackbar(requireView(), cartProductsState.message)
    }


    @SuppressLint("SetTextI18n")
    private fun setTotalPrice(totalPrice: Int) {
        binding.tvTotalPrice.text = "$$totalPrice"
    }

    private fun changeBtnOperation(changes: Boolean) {
        if (changes) binding.btnCheckoutOrUpdate.text = getString(R.string.g_update)
        else binding.btnCheckoutOrUpdate.text = getString(R.string.g_checkout)
    }

    private fun handleDeleteItemState(deleteState: Resource<Unit>) {
        when (deleteState) {
            is Resource.Error -> showAlongSnackbar(requireView(), deleteState.message)
            else -> Unit
        }
    }

    private fun showAlertDialog(cartProduct: CartProduct) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.g_delete_item))
            .setMessage(getString(R.string.g_delete_item_message))
            .setPositiveButton(getString(R.string.g_delete)) { dialog, _ ->
                lifecycleScope.launch { viewModel.deleteProducts(cartProduct) }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.g_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun handleUpdateCartCartProductsState(updateCartProductsState: Resource<String>) {
        when(updateCartProductsState) {
            is Resource.Loading -> binding.btnCheckoutOrUpdate.startAnimation()
            is Resource.Success -> handleSuccessUpdateCartProductsState(updateCartProductsState.data)
            is Resource.Error -> handleErrorUpdateCartProductsState(updateCartProductsState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessUpdateCartProductsState(message: String) {
        binding.btnCheckoutOrUpdate.revertAnimation()
        showAShortToast(requireContext(), message)
    }

    private fun handleErrorUpdateCartProductsState(message: String) {
        binding.btnCheckoutOrUpdate.revertAnimation()
        showAlongSnackbar(requireView(), message)
    }




}