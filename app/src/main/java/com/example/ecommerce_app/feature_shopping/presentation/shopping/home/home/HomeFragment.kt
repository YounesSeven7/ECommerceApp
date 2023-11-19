package com.example.ecommerce_app.feature_shopping.presentation.shopping.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentHomeBinding
import com.example.ecommerce_app.feature_shopping.data.model.Category
import com.example.ecommerce_app.feature_shopping.presentation.adapters.viewPager.CategoryViewPagerAdapter
import com.example.ecommerce_app.feature_shopping.presentation.shopping.home.category.CategoryFragment
import com.example.ecommerce_app.feature_shopping.presentation.showAlongSnackbarAboveBottomNav
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation

import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        lifecycleScope.apply {
            launch { viewModel.getAllCategoriesState.collect{ handleGetAllCategoriesState(it) } }

            launch { viewModel.getAllCategories() }
        }

    }


    private fun handleGetAllCategoriesState(getAllCategoriesState: Resource<List<Category>>) {
        when(getAllCategoriesState) {
            is Resource.Loading -> showThisView(binding.progressbarCategories)
            is Resource.Success -> handleSuccessGetAllCategoriesState(getAllCategoriesState)
            is Resource.Error -> handleErrorGetAllCategoriesState(getAllCategoriesState)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessGetAllCategoriesState(successGetAllCategoriesState: Resource.Success<List<Category>>) {
        viewModel.doWeHaveCategories = true
        hideThisView(binding.progressbarCategories)
        binding.apply { makeScreenReady(successGetAllCategoriesState.data) }
    }

    private fun FragmentHomeBinding.makeScreenReady(categories: List<Category>) {
        val fragments = makeCategoriesFragments(categories)
        val categoryViewPageAdapter = makeCategoryViewPagerAdapter(fragments)
        setupViewPager(categoryViewPageAdapter)
        setupTabLayout(categories)
    }
    private fun makeCategoriesFragments(categories: List<Category>) = categories.map { category ->
        CategoryFragment.newInstance(category.id)
    }
    private fun makeCategoryViewPagerAdapter(fragments: List<Fragment>) =
        CategoryViewPagerAdapter(
            fragments,
            childFragmentManager,
            lifecycle
        )
    private fun FragmentHomeBinding.setupViewPager(categoryViewPagerAdapter: CategoryViewPagerAdapter) {
        viewpagerHome.adapter = categoryViewPagerAdapter
        viewpagerHome.isUserInputEnabled = false
    }
    private fun FragmentHomeBinding.setupTabLayout(categories: List<Category>) {
        TabLayoutMediator(tabLayout, viewpagerHome) { tab, position ->
            tab.text = categories[position].name
        }.attach()
    }

    private fun handleErrorGetAllCategoriesState(errorGetAllCategoriesState: Resource.Error<List<Category>>) {
        hideThisView(binding.progressbarCategories)
        if (!viewModel.doWeHaveCategories) {
            showAlongSnackbarAboveBottomNav(errorGetAllCategoriesState.message)
        }
    }





}