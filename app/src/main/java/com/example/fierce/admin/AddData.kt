package com.example.fierce.admin

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fierce.R
import com.example.fierce.databinding.ActivityAddDataBinding
import com.example.fierce.model.Shoes
import com.example.fierce.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddData : AppCompatActivity() {
    private val binding by lazy{
        ActivityAddDataBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Setup Spinner for status
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.status_options, // Status options in res/values/strings.xml
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = spinnerAdapter

        // Create button listener
        binding.btnCreate.setOnClickListener {
            val brand = binding.etBrand.text.toString().trim()
            val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val size = binding.etSize.text.toString().toFloatOrNull() ?: 0.0f
            val detail = binding.etDetail.text.toString().trim()
            val image = binding.etImage.text.toString().trim()
            val status = binding.spinnerStatus.selectedItem.toString()

            // Validate inputs
            if (brand.isEmpty() || detail.isEmpty() || image.isEmpty() || price <= 0 || size <= 0) {
                Toast.makeText(this, "Please fill in all fields correctly!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create Shoes object
            val newShoe = Shoes(
                brand = brand,
                price = price,
                size = size,
                detail = detail,
                image = image,
                status = status
            )

            // API call to create a new shoe
            ApiClient.getInstance().addShoes(newShoe).enqueue(object : Callback<Shoes> {
                override fun onResponse(call: Call<Shoes>, response: Response<Shoes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddData, "Shoe created successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("CreateShoe", "Failed to create shoe: $errorBody")
                        Toast.makeText(this@AddData, "Failed to create shoe. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Shoes>, t: Throwable) {
                    Log.e("CreateShoe", "Error: ${t.message}")
                    Toast.makeText(this@AddData, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}