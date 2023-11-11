package com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.ecommerce_app.databinding.CartItemBinding
import com.example.ecommerce_app.databinding.ProductItemBinding
import com.example.ecommerce_app.feature_shopping.data.model.CartProduct
import com.example.ecommerce_app.feature_shopping.domain.util.getImageRequest

class CartProductAdapter(
    val imageLoader: ImageLoader,
    val increaseOrDecreaseQuantity: (cartProduct: CartProduct, increase: Boolean) -> Unit,
    val getNewQuantity: (CartProduct) -> Int,
) : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {


    private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.quantity == newItem.quantity
        }

    }

    val diff = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    inner class CartProductViewHolder(
        val binding: CartItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val cartProduct = diff.currentList[position]
            binding.apply {
                setImage(cartProduct.product.images[0])
                tvCartProductName.text = cartProduct.product.title
                tvProductCartPrice.text = "$${cartProduct.product.price.toString()}"
                setQuantity(getNewQuantity(cartProduct))
                imgPlus.setOnClickListener { imagePlusOnClick(cartProduct) }
                imgMinus.setOnClickListener { imageMinusOnClick(cartProduct) }
            }
        }

        private fun CartItemBinding.imagePlusOnClick(cartProduct: CartProduct) {
            increaseOrDecreaseQuantity(cartProduct, true)
            setQuantity(getNewQuantity(cartProduct))
        }

        private fun CartItemBinding.imageMinusOnClick(cartProduct: CartProduct) {
            increaseOrDecreaseQuantity(cartProduct, false)
            setQuantity(getNewQuantity(cartProduct))
        }

        private fun CartItemBinding.setQuantity(quantity: Int) {
            tvQuantity.text = quantity.toString()
        }


        private fun CartItemBinding.setImage(url: String) {
            val request = getImageRequest(imgCartProduct, url)
            imageLoader.enqueue(request)
        }

    }
}