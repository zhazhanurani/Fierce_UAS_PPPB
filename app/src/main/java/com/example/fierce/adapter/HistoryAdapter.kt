package com.example.fierce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fierce.R
import com.example.fierce.model.History
import com.example.fierce.model.Shoes

class HistoryAdapter(
    private val historyList : List<History>,
    private val onItemClick: (History) -> Unit,
    private val onItemLongClick: (History) -> Unit // Callback for long click to delete
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Deklarasikan elemen UI di sini
        val imageView : ImageView = itemView.findViewById(R.id.img_catalog)
        val brandTextView : TextView = itemView.findViewById(R.id.brand_catalog)
        val priceTextView : TextView = itemView.findViewById(R.id.price_catalog)
        val sizeTextView : TextView = itemView.findViewById(R.id.size_catalog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_catalog,parent,false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val shoes = historyList[position]

        Glide.with(holder.itemView.context)
            .load(shoes.image)
            .placeholder(R.drawable.ferce)
            .into(holder.imageView)

        val price_nominal = shoes.price.toInt()

        holder.brandTextView.text = shoes.brand
        holder.priceTextView.text = "Rp.${price_nominal}"
        holder.sizeTextView.text = shoes.size.toString()
        holder.itemView.setOnClickListener { onItemClick(shoes) }
        holder.itemView.setOnLongClickListener { onItemLongClick(shoes); true }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}