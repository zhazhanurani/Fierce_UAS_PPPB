package com.example.fierce.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fierce.R
import com.example.fierce.databinding.ActivityAdminBinding
import com.example.fierce.databinding.ActivityMainBinding

class AdminActivity : AppCompatActivity() {
    private val binding by  lazy{
        ActivityAdminBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view before anything else
        setContentView(binding.root)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Find the NavController after the content view is set
        val navController = findNavController(R.id.nav_host_fragment_container3)

        // Set up BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}