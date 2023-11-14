package com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.AddressItemBinding
import com.example.ecommerce_app.feature_shopping.data.model.Address

class AddressAdapter (
    val onChangeAddress: (newPosition: Int) -> Int
): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return true
        }

    }


    val diff = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }


    inner class AddressViewHolder(
        val binding: AddressItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val address = diff.currentList[position]


            binding.btnAddress.text = address.fullName
            binding.btnAddress.background = getColor(R.color.g_white)

            binding.btnAddress.setOnClickListener {
                val oldPosition = onChangeAddress(position)
                if (oldPosition >= 0 && oldPosition != position)
                    notifyItemChanged(oldPosition)
                it.background = getColor(R.color.g_black)
            }
        }

        private fun getColor(color: Int) =
            ColorDrawable(itemView.context.resources.getColor(color, null))
    }


}