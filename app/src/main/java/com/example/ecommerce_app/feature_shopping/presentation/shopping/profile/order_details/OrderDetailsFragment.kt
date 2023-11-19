package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.order_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.databinding.FragmentOrderDetailsBinding
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.data.model.OrderStatus
import com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView.BillingProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailsFragment: Fragment() {
    private lateinit var binding: FragmentOrderDetailsBinding
    private val args by navArgs<OrderDetailsFragmentArgs>()
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var billingProductAdapter: BillingProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }
    }

    private fun FragmentOrderDetailsBinding.makeScreenReady() {
        args.order.apply {
            setupOrderStep(orderStatus)
            setupAddress(address)
            setupCartProductsRv(cartProductList)
            setupTotalPrice(totalPrice)
        }
    }

    private fun FragmentOrderDetailsBinding.setupOrderStep(orderStatus: OrderStatus) {
        stepView.setStepsNumber(4)
        val currentOrderStep = getCurrentOrderStep(orderStatus)
        stepView.go(currentOrderStep, false)
        if (currentOrderStep == 3) stepView.done(true)
    }

    private fun getCurrentOrderStep(orderStatus: OrderStatus) = when(orderStatus) {
        OrderStatus.Ordered -> 0
        OrderStatus.Confirmed -> 1
        OrderStatus.Shipped -> 2
        OrderStatus.Delivered -> 3
        else -> 0
    }


    private fun FragmentOrderDetailsBinding.setupAddress(address: Address) {
        tvAddress.text = address.addressTitle
        tvFullName.text = address.fullName
        tvPhoneNumber.text = address.phone
    }

    private fun FragmentOrderDetailsBinding.setupCartProductsRv(cartProductList: List<CartProduct>) {
        billingProductAdapter = BillingProductAdapter(imageLoader)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvProducts.apply {
            adapter = billingProductAdapter
            layoutManager = linearLayoutManager
        }
        billingProductAdapter.diff.submitList(cartProductList)
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentOrderDetailsBinding.setupTotalPrice(totalPrice: Int) {
        tvTotalprice.text = "$$totalPrice"
    }
}