package com.example.ecommerce_app.feature_shopping.presentation.shopping.cart.billing

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showAShortToast
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentBillingBinding
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.data.model.Order
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.AddressAdapter
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.BillingProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var billingProductAdapter: BillingProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getAddressesState.collect { handleGetAddressState(it) } }

            launch { viewModel.getAllAddresses() }

            if (args.payment == null)
                launch { viewModel.currentSelectedAddressPosition.collect { handleErrorTvVisibility(it) } }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun FragmentBillingBinding.makeScreenReady() {
        setupAddressRv()
        setButtons()
        if (args.payment != null){
            setupProductRv()
            tvTotalprice.text = "$${args.payment!!.price}"
        } else {
            hideViews()
        }
    }

    private fun FragmentBillingBinding.hideViews() {
        hideThisView(rvProducts)
        hideThisView(linear)
        hideThisView(btnPlaceOrder)
        hideThisView(line2)
        hideThisView(tvSelectAddressError)
    }

    private fun setupAddressRv() {
        addressAdapter = AddressAdapter()
        if (args.payment != null)
            addressAdapter.onChangeAddress = { newPos -> viewModel.onChangeAddressPosition(newPos) }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvAdresses.apply {
            adapter = addressAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun setupProductRv() {
        billingProductAdapter = BillingProductAdapter(imageLoader)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            layoutManager = linearLayoutManager
        }
        val productsList = args.payment!!.cartProductsList.toList()
        billingProductAdapter.diff.submitList(productsList)
    }

    private fun FragmentBillingBinding.setButtons() {
        imgCloseBilling.setOnClickListener {
            findNavController().popBackStack()
        }
        imgAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        btnPlaceOrder.setOnClickListener {
            if (viewModel.currentSelectedAddressPosition.value == -1)
                showThisView(binding.tvSelectAddressError)
            else
                lifecycleScope.launch {
                    viewModel.placeOrder(getOrderInfo()).collect { handelPlaceOrderState(it) }
                }
        }
    }

    private fun getOrderInfo() = Order(
        cartProductList = args.payment!!.cartProductsList,
        totalPrice = args.payment!!.price,
        address = addressAdapter.diff.currentList[viewModel.currentSelectedAddressPosition.value]
    )

    private fun handelPlaceOrderState(placeOrderState: Resource<String>) {
        when (placeOrderState) {
            is Resource.Loading -> binding.btnPlaceOrder.startAnimation()
            is Resource.Success -> handleSuccessPlaceOrderState(placeOrderState.data)
            is Resource.Error -> handleErrorPlaceOrderState(placeOrderState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessPlaceOrderState(message: String) {
        showAShortToast(requireContext(), message)
        binding.btnPlaceOrder.revertAnimation {
            findNavController().popBackStack()
        }
    }

    private fun handleErrorPlaceOrderState(message: String) {
        Log.d("younes", message)
        showAlongSnackbar(requireView(), message)
        binding.btnPlaceOrder.revertAnimation()
    }


    private fun handleGetAddressState(getAllAddressesState: Resource<List<Address>>) {
        when (getAllAddressesState) {
            is Resource.Loading -> showThisView(binding.progressbarAddresses)
            is Resource.Success -> handleSuccessGetAllAddressesState(getAllAddressesState.data)
            is Resource.Error -> handleErrorGetAllAddressState(getAllAddressesState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessGetAllAddressesState(addressesList: List<Address>) {
        hideThisView(binding.progressbarAddresses)
        addressAdapter.diff.submitList(addressesList)
    }

    private fun handleErrorGetAllAddressState(message: String) {
        hideThisView(binding.progressbarAddresses)
        showAlongSnackbar(requireView(), message)
    }


    private fun handleErrorTvVisibility(currentPosition: Int) {
        if (currentPosition >= 0)
            hideThisView(binding.tvSelectAddressError)
        else
            showThisView(binding.tvSelectAddressError)
    }


}