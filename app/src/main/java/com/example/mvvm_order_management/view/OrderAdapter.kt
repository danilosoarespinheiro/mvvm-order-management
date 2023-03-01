package com.example.mvvm_order_management.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_order_management.databinding.OrderItemBinding
import com.example.mvvm_order_management.model.Order

class OrderAdapter(private val orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateOrders(newOrders : List<Order>){
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    inner class OrderViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.orderId.text = order.id.toString()
            binding.product.text = order.product.toString()
            binding.user.text = order.user.toString()
        }
    }
}