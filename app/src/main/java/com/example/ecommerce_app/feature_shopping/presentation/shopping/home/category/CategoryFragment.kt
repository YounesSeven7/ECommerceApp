package com.example.ecommerce_app.feature_shopping.presentation.shopping.home.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentHomeCategoryBinding
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.ProductAdapter
import com.example.ecommerce_app.feature_shopping.presentation.hideBottomNavigation
import com.example.ecommerce_app.feature_shopping.presentation.shopping.home.home.HomeFragmentDirections
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CategoryFragment: Fragment() {

    private lateinit var binding: FragmentHomeCategoryBinding
    private val viewModel by viewModels<CategoryViewModel>()
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var productAdapter: ProductAdapter

    val CATEGORY_ARGUMENT_KEY = "CATEGORY_ARGUMENT_KEY"

    private var categoryId = 0

    companion object {
        fun newInstance(categoryID: Int)= CategoryFragment().apply {
            arguments = Bundle().apply {
                putInt(CATEGORY_ARGUMENT_KEY, categoryID)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(CATEGORY_ARGUMENT_KEY)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getProductsState.collect { handleGetProductsState(it) } }

            launch { viewModel.getProductsByCategoryId(categoryId) }
        }

    }

    private fun FragmentHomeCategoryBinding.makeScreenReady() {
        productAdapter = ProductAdapter(imageLoader){ startDetailsFragment(it) }
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        rvProducts.apply {
            adapter = productAdapter
            layoutManager = gridLayoutManager
            addOnScrollListener(onScrollListener)
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            binding.apply {
                val currentPosition = getCurrentPosition()
                if (currentPosition + 6 >= viewModel.offset * 10) {
                    viewModel.offset++
                    lifecycleScope.launch { viewModel.getProductsByCategoryId(categoryId) }
                }
            }
        }

        private fun FragmentHomeCategoryBinding.getCurrentPosition(): Int {
            val offset: Int = rvProducts.computeVerticalScrollOffset()
            var position: Float = offset.toFloat() / (rvProducts.getChildAt(0).measuredHeight).toFloat()
            position += 0.5f
            return position.toInt() * 2
        }
    }

    private fun startDetailsFragment(product: Product) {
        val direction = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(product)
        findNavController().navigate(direction)
    }

    private fun handleGetProductsState(getProductsState: Resource<List<Product>>) {
        when(getProductsState) {
            is Resource.Loading -> showThisView(binding.progressbarProducts)
            is Resource.Success -> handleSuccessGetProductsState(getProductsState)
            is Resource.Error -> handleErrorGetProductsState(getProductsState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessGetProductsState(successProductsState: Resource.Success<List<Product>>) {
        hideThisView(binding.progressbarProducts)
        if (successProductsState.data.isEmpty())
            showThisView(binding.tvNoProductAvailable)
        else {
            hideThisView(binding.tvNoProductAvailable)
            productAdapter.diff.submitList(successProductsState.data)
        }

    }

    private fun handleErrorGetProductsState(errorProductsState: Resource.Error<List<Product>>) {
        if (viewModel.productsList.isEmpty()) {
            hideThisView(binding.progressbarProducts)
            showThisView(binding.tvNoProductAvailable)
        } else {
            showAlongSnackbar(requireView(), errorProductsState.message)
        }
    }


}