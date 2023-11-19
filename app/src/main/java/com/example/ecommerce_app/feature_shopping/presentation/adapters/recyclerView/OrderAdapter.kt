package com.example.ecommerce_app.feature_shopping.presentation.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.databinding.OrderItemBinding
import com.example.ecommerce_app.feature_shopping.data.model.Order

class OrderAdapter(
    private val onClick: (order: Order) -> Unit
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.data == newItem.id
        }

    }

    val diff = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapter.OrderViewHolder {
        return OrderViewHolder(
            OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    inner class OrderViewHolder(
        val binding: OrderItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val order = diff.currentList[position]
            binding.apply {
                tvOrderId.text = order.id
                tvOrderDate.text = order.data
                parentLinear.setOnClickListener { onClick(order) }
            }
        }
    }


}