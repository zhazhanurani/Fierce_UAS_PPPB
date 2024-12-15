package com.example.fierce.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fierce.LoginActivity
import com.example.fierce.R
import com.example.fierce.databinding.FragmentAdminAccountBinding
import com.example.fierce.sharedPref.PrefManager

class AdminAccount : Fragment() {
    private val binding by lazy{
        FragmentAdminAccountBinding.inflate(layoutInflater)
    }

    private lateinit var prefManager: PrefManager

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

        prefManager = PrefManager.getInstance(requireContext())

        binding.accUsername.text = prefManager.getUsername()
        binding.accEmail.text = prefManager.getEmail()
        binding.accAdress.text = prefManager.getAddress()

        binding.btnLogout.setOnClickListener {
            prefManager.clearUser()
            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentToLogin)
        }
    }
}