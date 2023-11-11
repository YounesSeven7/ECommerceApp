package com.example.ecommerce_app.feature_shopping.presentation.shopping.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentSearchBinding
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.ProductAdapter
import com.example.ecommerce_app.feature_shopping.presentation.hideBottomNavigation
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var productAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getProductByNameState.collect{ handleGetProductsState(it) } }
        }
    }

    private fun FragmentSearchBinding.makeScreenReady() {
        showBottomNavigation()
        productAdapter = ProductAdapter(imageLoader){ startDetailsFragment(it) }
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        rvSearch.apply {
            adapter = productAdapter
            layoutManager = gridLayoutManager
        }
        setupSearchET()
    }

    private fun startDetailsFragment(product: Product) {
        val direction = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(product)
        findNavController().navigate(direction)
        hideBottomNavigation()
    }

    private fun FragmentSearchBinding.setupSearchET() {
        edSearch.apply {
            requestFocus()
            addTextChangedListener { s ->
                if (s.toString().isNotEmpty()) {
                    lifecycleScope.launch { viewModel.getProductByName(s.toString()) }
                } else {
                    productAdapter.diff.submitList(emptyList())
                }
            }
        }
    }

    private fun handleGetProductsState(getProductsState: Resource<List<Product>>) {
        when(getProductsState) {
            is Resource.Loading -> handleLoadingGetProductsState()
            is Resource.Success -> handleSuccessGetProductsState(getProductsState)
            is Resource.Error -> handleErrorGetProductsState(getProductsState)
            is Resource.Unspecified -> Unit
        }
    }



    private fun handleLoadingGetProductsState() {
        showThisView(binding.progressbarProducts)
        hideThisView(binding.tvNoProductAvailable)
    }

    private fun handleSuccessGetProductsState(successProductsState: Resource.Success<List<Product>>) {
        hideThisView(binding.progressbarProducts)

        if (binding.edSearch.text.isEmpty()) {
            productAdapter.diff.submitList(emptyList())
            return
        }

        val productsList = successProductsState.data

        if (productsList.isEmpty()) showThisView(binding.tvNoProductAvailable)
        else hideThisView(binding.tvNoProductAvailable)

        productAdapter.diff.submitList(productsList)
    }

    private fun handleErrorGetProductsState(errorProductsState: Resource.Error<List<Product>>) {
        hideThisView(binding.progressbarProducts)
        showAlongSnackbar(requireView(), errorProductsState.message)
    }




}