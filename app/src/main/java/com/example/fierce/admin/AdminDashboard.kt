package com.example.fierce.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fierce.R
import com.example.fierce.adapter.AdminAdapter
import com.example.fierce.adapter.ShoesAdapter
import com.example.fierce.databinding.FragmentAdminDashboardBinding
import com.example.fierce.model.Shoes
import com.example.fierce.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminDashboard : Fragment() {
    private val binding by lazy {
        FragmentAdminDashboardBinding.inflate(layoutInflater)
    }

    // Membuat variabel untuk menyimpan PrefManager
    private lateinit var adminAdapter : AdminAdapter
    private val shoesList = mutableListOf<Shoes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adminAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchShoesData()

        binding.btnAddShoes.setOnClickListener {
            val intent = Intent(requireContext(), AddData::class.java)
            startActivity(intent)
        }
    }

    fun setupRecyclerView() {
        adminAdapter = AdminAdapter(
            onEditClick = { shoes ->
                // Navigate to EditData activity or fragment
                val intent = Intent(requireContext(), EditData::class.java).apply {
                    putExtra("id", shoes.id ?: "")
                    putExtra("brand", shoes.brand ?: "")
                    putExtra("size", shoes.size ?: 0.0f)
                    putExtra("status", shoes.status ?: "")
                    putExtra("alamat", shoes.image ?: "")
                    putExtra("harga", shoes.price ?: 0.0)
                    putExtra("detail", shoes.detail ?: "")
                }
                startActivity(intent)
            },
            onDeleteClick = { shoes ->
                // Call delete function
                deleteShoes(shoes)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set a layout manager
        binding.recyclerView.adapter = adminAdapter // Set the adapter
    }


    fun fetchShoesData(){
        val client = ApiClient.getInstance()

        client.getAllShoes().enqueue(object : Callback<List<Shoes>> {
            override fun onResponse(call: Call<List<Shoes>>, response: Response<List<Shoes>>) {
                if (response.isSuccessful && response.body() != null) {
                    val shoes = response.body()
                    Log.d("AdminDashboard", "Received data: ${shoes?.size}")

                    shoes?.let {
                        shoesList.clear()
                        shoesList.addAll(it)
                        adminAdapter.submitList(shoesList)
                        adminAdapter.notifyDataSetChanged() // Notify adapter about the change
                    }
                } else {
                    Log.e("AdminDashboard", "Response failed: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load shoes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Shoes>>, t: Throwable) {
                Log.e("AdminDashboard", "Network request failed: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun deleteShoes(shoes: Shoes) {
        val client = ApiClient.getInstance()

        if (shoes.id.isNullOrEmpty()) {
            Log.e("AdminDashboard", "Shoe id is null or empty, cannot delete")
            Toast.makeText(requireContext(), "Invalid shoe ID", Toast.LENGTH_SHORT).show()
            return
        }

        client.deleteShoes(shoes.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Remove the deleted item from the list
                    shoesList.remove(shoes)
                    adminAdapter.submitList(shoesList) // Update the adapter list
                    Toast.makeText(requireContext(), "Shoe deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete shoe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AdminDashboard", "Failed to delete shoe: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}