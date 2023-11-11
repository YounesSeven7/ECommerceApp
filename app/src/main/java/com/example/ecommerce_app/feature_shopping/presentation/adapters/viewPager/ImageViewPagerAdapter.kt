package com.example.ecommerce_app.feature_shopping.presentation.adapters.viewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.databinding.ViewpagerImageItemBinding
import com.example.ecommerce_app.feature_shopping.domain.util.getImageRequest

class ImageViewPagerAdapter(
    private val imageLoader: ImageLoader,
    private val imageList: List<String>
): RecyclerView.Adapter<ImageViewPagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageViewHolder(
        private val binding: ViewpagerImageItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl : String) {
            val request = getImageRequest(binding.imgsProduct, imageUrl)
            imageLoader.enqueue(request)
        }
    }
}