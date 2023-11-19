package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.util.Resource
import com.example.ecommerce_app.databinding.FragmentProfileBinding
import com.example.ecommerce_app.feature_auth.presentation.launch.LaunchActivity
import com.example.ecommerce_app.feature_shopping.domain.util.getImageRequest
import com.example.ecommerce_app.feature_shopping.presentation.showAlongSnackbarAboveBottomNav
import com.example.ecommerce_app.feature_shopping.presentation.showBottomNavigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    @Inject lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { makeScreenReady() }

        lifecycleScope.apply {
            launch { viewModel.getUserState.collect{ handleGetUserState(it) } }

            launch { viewModel.getUser() }
        }
    }



    private fun FragmentProfileBinding.makeScreenReady() {
        allOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }
        linearTrackOrder.setOnClickListener {
            showAlongSnackbarAboveBottomNav(getString(R.string.feature_not_available))
        }
        linearBilling.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_billingFragment)
        }
        linearLanguage.setOnClickListener {
            Intent("android.settings.APP_LOCALE_SETTINGS").apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
                startActivity(this)
            }
        }
        linearLogout.setOnClickListener {
            viewModel.logout()
            startLaunchActivity()
        }
    }

    private fun startLaunchActivity() {
        Intent(requireContext(), LaunchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    private fun handleGetUserState(getUserState: Resource<User>) {
        when(getUserState) {
            is Resource.Loading -> Unit
            is Resource.Success -> handleSuccessGetUserState(getUserState.data)
            is Resource.Error -> showAlongSnackbarAboveBottomNav(getUserState.message)
            is Resource.Unspecified -> Unit
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleSuccessGetUserState(user: User) {
        binding.apply {
            if (user.imageUrl.isNotEmpty()) setImage(user.imageUrl)
            tvUserName.text = "${user.firstName} ${user.lastName}"
            constraintProfile.setOnClickListener {
                Log.d("younes", "younes")
                val code = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user)
                findNavController().navigate(code)
            }
        }
    }

    private fun FragmentProfileBinding.setImage(imageUrl: String) {
        val request = getImageRequest(imgUser, imageUrl)
        imageLoader.enqueue(request)
    }

}