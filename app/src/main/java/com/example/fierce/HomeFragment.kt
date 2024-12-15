package com.example.fierce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fierce.adapter.ShoesAdapter
import com.example.fierce.databinding.FragmentHomeBinding
import com.example.fierce.model.Shoes
import com.example.fierce.network.ApiClient
import com.example.fierce.sharedPref.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private val binding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }

    // Membuat variabel untuk menyimpan PrefManager
    private lateinit var shoesAdapter: ShoesAdapter
    private val shoesList = mutableListOf<Shoes>()
    private val filteredShoesList = mutableListOf<Shoes>()
    private var currentFilter: String = "All"


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchShoesData()
        setupRecyclerView()
    }

    fun setupRecyclerView(){
        shoesAdapter = ShoesAdapter(filteredShoesList){ shoes->
            val bundle = Bundle().apply {
                putString("id", shoes.id)
                putString("brand", shoes.brand)
                putString("status", shoes.status)
                putFloat("size", shoes.size)
                putDouble("price", shoes.price)
                putString("detail", shoes.detail)
                putString("image", shoes.image)
            }
            findNavController().navigate(R.id.action_homeFragment_to_fragmentDetails, bundle)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = shoesAdapter
        }

        binding.btnReady.setOnClickListener {
            // If "ready" is clicked again, reset to "All"
            currentFilter = if (currentFilter == "ready") "All" else "ready"
            filterMenu(currentFilter)
        }

        binding.btnOnlist.setOnClickListener {
            // If "on_list" is clicked again, reset to "All"
            currentFilter = if (currentFilter == "on_list") "All" else "on_list"
            filterMenu(currentFilter)
        }

        // Initially show all items
        filterMenu("All")
    }

    private fun filterMenu(type: String) {
        filteredShoesList.clear()

        if (type == "All") {
            filteredShoesList.addAll(shoesList)
        } else {
            filteredShoesList.addAll(shoesList.filter { it.status.lowercase() == type.lowercase() })
        }

        // Show or hide the "No Item In This Catalog" TextView
        if (filteredShoesList.isEmpty()) {
            binding.txtNoItems.visibility = View.VISIBLE
        } else {
            binding.txtNoItems.visibility = View.GONE
        }
        shoesAdapter.notifyDataSetChanged()
    }


    fun fetchShoesData(){
        val client = ApiClient.getInstance()

        client.getAllShoes().enqueue(object : Callback<List<Shoes>> {
            override fun onResponse(call: Call<List<Shoes>>, response: Response<List<Shoes>>) {
                if (response.isSuccessful && response.body() != null) {
                    val menus = response.body()
                    menus?.let {
                        shoesList.clear() // Clear previous data
                        shoesList.addAll(it) // Add all menu items to the list
                        shoesAdapter.notifyDataSetChanged() // Notify adapter about data change
                        filterMenu("All")
                    }
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load menus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Shoes>>, t: Throwable) {
                Log.e("HomeFragment", "Network request failed: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}