package com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.databinding.ProductItemBinding
import com.example.ecommerce_app.feature_shopping.data.model.Product
import com.example.ecommerce_app.feature_shopping.domain.util.getImageRequest

class ProductAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (Product) -> Unit
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val diff = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    inner class ProductViewHolder(
        val binding: ProductItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val product = diff.currentList[position]
            binding.apply {
                setImage(product.images[0])
                tvName.text = product.title
                tvPrice.text = "$${product.price}"
                binding.root.setOnClickListener { onClick(product) }
            }
        }

        private fun ProductItemBinding.setImage(url: String) {
            val request = getImageRequest(imgProduct, url)
            imageLoader.enqueue(request)
        }

    }

}