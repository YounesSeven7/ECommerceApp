package com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.databinding.BillingProductItemBinding
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.domain.util.getImageRequest

class BillingProductAdapter (
    val imageLoader: ImageLoader
) : RecyclerView.Adapter<BillingProductAdapter.ProductViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.title == newItem.product.title
        }

    }

    val diff = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            BillingProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    inner class ProductViewHolder(
        val binding: BillingProductItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val cartProduct = diff.currentList[position]
            binding.apply {
                tvProductCartName.text = cartProduct.product.title
                tvProductCartPrice.text = "$${cartProduct.product.price}"
                tvBillingProductQuantity.text = cartProduct.quantity.toString()
                setImage(cartProduct.product.images[0])
            }
        }

        private fun BillingProductItemBinding.setImage(url: String) {
            val request = getImageRequest(imageCartProduct, url)
            imageLoader.enqueue(request)
        }
    }

}