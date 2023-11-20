package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.core.presentation.hideThisView
import com.example.ecommerce_app.core.presentation.showAShortToast
import com.example.ecommerce_app.core.presentation.showAlongSnackbar
import com.example.ecommerce_app.core.presentation.showThisView
import com.example.ecommerce_app.databinding.FragmentAddressBinding
import com.example.ecommerce_app.feature_auth.presentation.utile.setErrorMessageOnEditText
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.domain.util.AddressFields
import com.example.ecommerce_app.feature_shopping.presentation.hideBottomNavigation
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment: Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()
    private val args by navArgs<AddressFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun FragmentAddressBinding.makeScreenReady() {
        hideBottomNavigation()
        if (args.address == null) {
            makeAddBtnReady()
        } else {
            makeUpdateDeleteBtnReady(args.address!!)
        }
    }

    private fun FragmentAddressBinding.makeAddBtnReady() {
        hideThisView(updateDeleteBtn)
        showThisView(btnAddAddress)
        btnAddAddress.setOnClickListener {
            val newAddress = getDateFromEditTexts()
            lifecycleScope.launch { viewModel.addAddress(newAddress) }
        }
    }

    private fun FragmentAddressBinding.makeUpdateDeleteBtnReady(address: Address) {
        hideThisView(btnAddAddress)
        showThisView(updateDeleteBtn)
        setupDataOnEditTexts(address)
        btnUpdateAddress.setOnClickListener {
            val newAddress = getDateFromEditTexts()
            lifecycleScope.launch { viewModel.updateAddress(newAddress, address) }
        }
        btnDeleteAddress.setOnClickListener {
            lifecycleScope.launch{ viewModel.deleteAddress(address) }
        }
    }

    private fun FragmentAddressBinding.setupDataOnEditTexts(address: Address) {
        edAddressTitle.setText(address.addressTitle)
        edFullName.setText(address.fullName)
        edStreet.setText(address.street)
        edPhone.setText(address.phone)
        edCity.setText(address.city)
        edState.setText(address.state)
    }

    private fun FragmentAddressBinding.getDateFromEditTexts(): Address {
        val addressTitle = edAddressTitle.text.toString()
        val fullName = edFullName.text.toString()
        val street = edStreet.text.toString()
        val phone = edPhone.text.toString()
        val city = edCity.text.toString()
        val state = edState.text.toString()
        return Address(addressTitle, fullName, street, phone, city, state)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            if (args.address == null) {
                launch { viewModel.addAddressState.collect { handleAddAddressState(it) } }
            } else {
                launch { viewModel.updateAddressState.collect { handleUpdateAddressState(it) } }

                launch { viewModel.deleteAddressState.collect{ handleDeleteAddressState(it) } }
            }

            launch { viewModel.addressFieldsValidation.collect{ handleAddressFieldsValidation(it) } }

        }
    }

    private fun handleAddAddressState(addAddressState: Resource<String>) {
        when(addAddressState) {
            is Resource.Loading -> binding.btnAddAddress.startAnimation()
            is Resource.Success -> handleSuccessAddAddressState(addAddressState.data)
            is Resource.Error -> showAlongSnackbar(requireView(), addAddressState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessAddAddressState(successMessage: String) {
        showAShortToast(requireContext(), successMessage)
        binding.btnAddAddress.revertAnimation{ findNavController().popBackStack() }
    }

    private fun handleUpdateAddressState(updateAddressState: Resource<String>) {
        when(updateAddressState) {
            is Resource.Loading -> binding.btnUpdateAddress.startAnimation()
            is Resource.Success -> handleSuccessUpdateAddressState(updateAddressState.data)
            is Resource.Error -> showAlongSnackbar(requireView(), updateAddressState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessUpdateAddressState(successMessage: String) {
        showAShortToast(requireContext(), successMessage)
        binding.btnUpdateAddress.revertAnimation{ findNavController().popBackStack() }
    }

    private fun handleDeleteAddressState(deleteAddressState: Resource<String>) {
        when(deleteAddressState) {
            is Resource.Loading -> binding.btnDeleteAddress.startAnimation()
            is Resource.Success -> handleSuccessDeleteAddressState(deleteAddressState.data)
            is Resource.Error -> showAlongSnackbar(requireView(), deleteAddressState.message)
            is Resource.Unspecified -> Unit
        }
    }

    private fun handleSuccessDeleteAddressState(successMessage: String) {
        showAShortToast(requireContext(), successMessage)
        binding.btnDeleteAddress.revertAnimation{ findNavController().popBackStack() }
    }


    private fun handleAddressFieldsValidation(addressFields: AddressFields) {
        binding.apply {
            addressFields.apply {
                if (!addressLocation.validation)
                    setErrorMessageOnEditText(edAddressTitle, addressLocation.message)

                else if (!fullName.validation)
                    setErrorMessageOnEditText(edFullName, fullName.message)

                else if (!street.validation)
                    setErrorMessageOnEditText(edStreet, street.message)

                else if (!phoneNumber.validation)
                    setErrorMessageOnEditText(edPhone, phoneNumber.message)

                else if (!cityName.validation)
                    setErrorMessageOnEditText(edCity, cityName.message)

                else if (!stateName.validation)
                    setErrorMessageOnEditText(edState, stateName.message)
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showBottomNavigation()
    }

}