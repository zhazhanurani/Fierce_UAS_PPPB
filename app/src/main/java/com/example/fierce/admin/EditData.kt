package com.example.fierce.admin

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fierce.R
import com.example.fierce.databinding.ActivityEditDataBinding
import com.example.fierce.model.Shoes
import com.example.fierce.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditData : AppCompatActivity() {

    private val binding by lazy { ActivityEditDataBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Retrieve data from Intent
        val id = intent.getStringExtra("id") ?: ""
        val brand = intent.getStringExtra("brand") ?: ""
        val size = intent.getFloatExtra("size", 0.0f)
        val image = intent.getStringExtra("alamat") ?: ""
        val price = intent.getDoubleExtra("harga", 0.0)
        val detail = intent.getStringExtra("detail") ?: ""
        val status = intent.getStringExtra("status") ?: "Ready"

        Log.d("EditData", "ID: $id, Brand: $brand, Size: $size, Image: $image, Price: $price, Detail: $detail, Status: $status")

        // Populate fields with received data
        binding.etBrand.setText(brand)
        binding.etPrice.setText(price.toString())
        binding.etSize.setText(size.toString())
        binding.etDetail.setText(detail)
        binding.etImage.setText(image)

        // Setup Spinner for status
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.status_options, // Values in res/values/strings.xml
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = spinnerAdapter

        // Set initial selection based on received status
        val position = when (status) {
            "On List" -> 1
            else -> 0 // Default to "Ready"
        }
        binding.spinnerStatus.setSelection(position)

        // Update button listener
        binding.btnUpdate.setOnClickListener {
            val updatedBrand = binding.etBrand.text.toString()
            val updatedPrice = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val updatedSize = binding.etSize.text.toString().toFloatOrNull() ?: 0.0f
            val updatedDetail = binding.etDetail.text.toString()
            val updatedImage = binding.etImage.text.toString()
            val updatedStatus = binding.spinnerStatus.selectedItem.toString()

            // Ensure that id is not empty or null
            if (id.isNullOrEmpty()) {
                Toast.makeText(this@EditData, "Invalid Shoe ID.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create the updated Shoes object
            val updatedShoe = Shoes(
                id = id,
                brand = updatedBrand,
                price = updatedPrice,
                size = updatedSize,
                detail = updatedDetail,
                image = updatedImage,
                status = updatedStatus
            )

            // Log the full API URL
            val url = "https://ppapb-a-api.vercel.app/mUTpQ/shoes/$id"
            Log.d("EditData", "API URL: $url")

            ApiClient.getInstance().updateShoes(id, updatedShoe).enqueue(object : Callback<Shoes> {
                override fun onResponse(call: Call<Shoes>, response: Response<Shoes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditData, "Shoe updated successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("EditData", "Failed to update shoe. Response code: ${response.code()} - $errorBody")
                        Toast.makeText(this@EditData, "Failed to update shoe: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Shoes>, t: Throwable) {
                    Log.e("EditData", "Error: ${t.message}")
                    Toast.makeText(this@EditData, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }

    }
}
