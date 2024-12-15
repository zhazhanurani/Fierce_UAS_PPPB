package com.example.fierce.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fierce.R
import com.example.fierce.admin.AdminActivity
import com.example.fierce.databinding.FragmentLoginBinding
import com.example.fierce.sharedPref.PrefManager


class LoginFragment : Fragment() {
    private val binding by lazy{
        FragmentLoginBinding.inflate(layoutInflater)
    }

    // membuat variabel untuk menyimpan PrefManager
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

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        // Cek Apakah Sudah Login Sebelumnya
        checkLoginStatus()

        with(binding){
            menujuRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnLogin.setOnClickListener{
                loginUser()
            }
        }
    }

    private fun loginUser(){
        val username = binding.loginUsername.text.toString()
        val password = binding.loginPassword.text.toString()


        // Get Saved Pref
        val savedUsername = prefManager.getUsername()
        val savedPassword = prefManager.getPassword()

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (prefManager.isAdmin(username, password)) {
            // If admin, navigate to AdminFragment
            findNavController().navigate(R.id.action_loginFragment_to_adminActivity)
            return
        }

        // Check if the user is registered
        if (savedUsername == null || savedPassword == null) {
            Toast.makeText(requireContext(), "No registered user found. Please register first.", Toast.LENGTH_SHORT).show()
            return
        }

        if (username == savedUsername && password == savedPassword) {
            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
            // Navigate to the main/home screen of your app
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        } else {
            Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLoginStatus(){
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        if (username != null && password != null){
            // Navigate to the main/home screen of your app
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }
    }


}