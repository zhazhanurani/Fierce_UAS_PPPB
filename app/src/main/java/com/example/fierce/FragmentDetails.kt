package com.example.fierce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fierce.database.HistoryDao
import com.example.fierce.database.HistoryDatabase
import com.example.fierce.databinding.FragmentDetailsBinding
import com.example.fierce.model.History
import com.example.fierce.model.Shoes
import com.example.fierce.network.ApiClient
import com.example.fierce.network.ApiService
import com.example.fierce.sharedPref.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FragmentDetails : Fragment() {
    private val binding by lazy{
        FragmentDetailsBinding.inflate(layoutInflater)
    }
    // Initiate Pref Manager and Database
    private lateinit var prefManager: PrefManager
    private lateinit var executorService: ExecutorService
    private lateinit var historyDao: HistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inisiasi Database
        executorService = Executors.newSingleThreadExecutor()
        val db = HistoryDatabase.getDatabase(requireContext())
        historyDao = db!!.historyDao()
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
        // inisiasi PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        // Province-based shipping costs
        val provincePriceMap = mapOf(
            "Jakarta" to 30000.0,
            "Bandung" to 20000.0,
            "Surabaya" to 45000.0,
            "Bali" to 60000.0,
            "Solo" to 25000.0,
            "Semarang" to 35000.0
        )
        val province = prefManager.getProvince()
        val address = prefManager.getAddress()


        // Get Data Passed From Home Fragment
        val id = arguments?.getString("id")
        val brand = arguments?.getString("brand")
        val status = arguments?.getString("status")
        val size = arguments?.getFloat("size")
        val price = arguments?.getDouble("price") ?: 0.0
        val detail = arguments?.getString("detail")
        val image = arguments?.getString("image")

        // Get shipping price for the province
        val shippingCost = provincePriceMap[province] ?: 0.0 // Default to 0 if not found
        val totalPrice = price.plus(shippingCost)


        // Bind Data
        with(binding){
            idItems.text = id.toString()
            textBrand.text = brand
            billId.text =  id.toString()
            description.text = detail
            txtSize.text = size.toString()
            billPriceItem.text = price.toString()
            priceTotal.text = totalPrice.toString()
            billDelivery.text = shippingCost.toString()
            billAddress.text = address.toString()

            if (status != "on_list"){
                btnBuyNow.isEnabled = true
            }else{
                btnBuyNow.isEnabled = false
                btnBuyNow.text = "Already Purchased"
            }

            Glide.with(requireContext())
                .load(image) // Image URL or path
                .placeholder(R.drawable.fierce_2) // Placeholder image
                .error(R.drawable.fierce_1) // Error image if loading fails
                .into(picCatalog)


            btnBuyNow.setOnClickListener {
                InsertToHistory(
                    History(
                        brand = brand.toString(),
                        price = price!!,
                        size = size!!,
                        detail = detail.toString(),
                        image = image.toString(),
                        status = "on_list",
                        totalPrice = totalPrice,
                        shippingCost = shippingCost,
                        address = address.toString(),
                    )
                )
            }
        }
    }
    fun InsertToHistory(history: History) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Insert the history record into the local database
                historyDao.insertHistory(history)

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "${history.brand} has been bought for ${history.totalPrice}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error buying ${history.brand} from the shop",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("InsertHistory", "Error inserting History: ${e.localizedMessage}", e)
                }
            }
        }
    }

    fun updateShoeStatus(id: String) {
        val client = ApiClient.getInstance()
        if (client == null) {
            Log.e("API", "ApiClient is null.")
            return
        }


        // Make the API request to update the shoe status
        client.updateShoesStatus(id, "on_list").enqueue(object : Callback<Shoes> {
            override fun onResponse(call: Call<Shoes>, response: Response<Shoes>) {
                if (response.isSuccessful) {
                    val updatedShoe = response.body()
                    if (updatedShoe != null) {
                        // If the API update was successful, update the UI
                        Log.d("UpdateShoeStatus", "Shoe status updated successfully: ${updatedShoe.status}")

                        // Optionally update local database or RecyclerView
                        // For example:
//                         historyList[position].status = "on_list"
//                         historyAdapter.notifyItemChanged(position)

                        // Show a success message
                        Toast.makeText(requireContext(), "Shoe status updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("UpdateShoeStatus", "API returned a null shoe object.")
                    }
                } else {
                    // API failed
                    Log.e("UpdateShoeStatus", "API call failed with status code: ${response.code()}")
                    Toast.makeText(requireContext(), "Failed to update shoe status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Shoes>, t: Throwable) {
                // Hide the loading indicator if there was a failure
                Log.e("UpdateShoeStatus", "Error updating shoe status: ${t.localizedMessage}", t)
                Toast.makeText(requireContext(), "Network error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }


}