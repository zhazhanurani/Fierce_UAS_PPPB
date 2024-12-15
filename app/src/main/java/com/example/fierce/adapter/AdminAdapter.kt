package com.example.fierce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fierce.databinding.ItemCatalogAdminBinding
import com.example.fierce.model.Shoes

class AdminAdapter(
    private val onEditClick: (Shoes) -> Unit,
    private val onDeleteClick: (Shoes) -> Unit
) : ListAdapter<Shoes, AdminAdapter.AdminViewHolder>(DiffCallback) {

    // DiffUtil for efficient list updates
    object DiffCallback : DiffUtil.ItemCallback<Shoes>() {
        override fun areItemsTheSame(oldItem: Shoes, newItem: Shoes): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Shoes, newItem: Shoes): Boolean = oldItem == newItem
    }

    inner class AdminViewHolder(private val binding: ItemCatalogAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Shoes, position: Int) {
            with(binding) {
                adminCatalog.text = "${position + 1}."
                adminBrand.text = data.brand

                // Set click listeners
                editItem.setOnClickListener { onEditClick(data) }
                deleteItem.setOnClickListener { onDeleteClick(data) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val binding = ItemCatalogAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}
