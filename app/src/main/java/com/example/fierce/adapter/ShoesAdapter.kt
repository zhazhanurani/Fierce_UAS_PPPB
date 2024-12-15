package com.example.fierce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fierce.R
import com.example.fierce.model.Shoes

class ShoesAdapter(
    private val shoesList: List<Shoes>,
    private val onItemClick: (Shoes) -> Unit
) : RecyclerView.Adapter<ShoesAdapter.ShoesViewHolder>() {

    class ShoesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Deklarasikan elemen UI di sini
        val imageView : ImageView = itemView.findViewById(R.id.img_catalog)
        val brandTextView : TextView = itemView.findViewById(R.id.brand_catalog)
        val priceTextView : TextView = itemView.findViewById(R.id.price_catalog)
        val sizeTextView : TextView = itemView.findViewById(R.id.size_catalog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_catalog,parent,false)
        return ShoesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoesViewHolder, position: Int) {
        val shoes = shoesList[position]

        Glide.with(holder.itemView.context)
            .load(shoes.image)
            .placeholder(R.drawable.ferce)
            .into(holder.imageView)

        val price_nominal = shoes.price.toInt()

        holder.brandTextView.text = shoes.brand
        holder.priceTextView.text = "Rp.${price_nominal}"
        holder.sizeTextView.text = shoes.size.toString()
        holder.itemView.setOnClickListener { onItemClick(shoes) }


    }

    override fun getItemCount(): Int {
        return shoesList.size
    }
}