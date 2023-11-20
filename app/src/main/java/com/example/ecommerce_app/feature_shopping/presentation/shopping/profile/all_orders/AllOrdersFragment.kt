package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.all_orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentAllOrdersBinding
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.OrderAdapter
import com.example.ecommerce_app.feature_shopping.presentation.hideBottomNavigation
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment: Fragment() {

    private lateinit var binding: FragmentAllOrdersBinding
    private val viewModel by viewModels<AllOrdersViewModel>()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getAllOrdersState.collect{ handleGetAllOrdersState(it) } }

            launch { viewModel.listenToOrders() }
        }
    }

    private fun FragmentAllOrdersBinding.makeScreenReady() {
        hideBottomNavigation()
        setupAllOrdersRv()
        setupButtons()
    }

    private fun FragmentAllOrdersBinding.setupAllOrdersRv() {
        orderAdapter = OrderAdapter { order ->
            val code = AllOrdersFragmentDirections.actionAllOrdersFragmentToOrderDetailsFragment(order)
            findNavController().navigate(code)
        }
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAllOrders.apply {
            adapter = orderAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun FragmentAllOrdersBinding.setupButtons() {
        imgCloseOrders.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleGetAllOrdersState(getAllOrdersState: Resource<List<Order>>) {
        when(getAllOrdersState) {
            is Resource.Loading -> showThisView(binding.progressbarAllOrders)
            is Resource.Success -> handleSuccessGetAllOrdersState(getAllOrdersState.data)
            is Resource.Error -> handleErrorGetAllOrdersState(getAllOrdersState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessGetAllOrdersState(ordersLists: List<Order>) {
        binding.apply {
            hideThisView(progressbarAllOrders)
            if (ordersLists.isNotEmpty()) {
                hideThisView(containerEmptyBox)
                showThisView(rvAllOrders)
                orderAdapter.diff.submitList(ordersLists)
            } else {
                showThisView(containerEmptyBox)
                hideThisView(rvAllOrders)
            }
        }
    }

    private fun handleErrorGetAllOrdersState(message: String) {
        hideThisView(binding.progressbarAllOrders)
        showAlongSnackbar(requireView(), message)
    }


    override fun onDestroy() {
        super.onDestroy()
        showBottomNavigation()
    }

}