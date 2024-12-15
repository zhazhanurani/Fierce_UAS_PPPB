package com.example.fierce.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fierce.R
import com.example.fierce.databinding.FragmentRegisterBinding
import com.example.fierce.sharedPref.PrefManager

class RegisterFragment : Fragment() {
    private val binding by lazy{
        FragmentRegisterBinding.inflate(layoutInflater)
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

        with(binding){
            btnRegister.setOnClickListener {
                registerUser()
            }

            menujuLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    fun registerUser(){
        val username = binding.registerUsername.text.toString()
        val email = binding.registerEmail.text.toString()
        val phone = binding.registerPhone.text.toString()
        val password = binding.registerPassword.text.toString()
        val confirmPassword = binding.registerCfPassword
            .text.toString()


        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()){
            binding.registerUsername.error = "Username tidak boleh kosong"
            binding.registerEmail.error = "Email tidak boleh kosong"
            binding.registerPhone.error = "Phone tidak boleh kosong"
            binding.registerPassword.error = "Password tidak boleh kosong"
            return
        }

        if (password != confirmPassword){
            binding.registerCfPassword.error = "Password tidak sama"
            return
        }

        // Optional: Check if a user is already registered
        if (prefManager.getUsername() != null) {
            Toast.makeText(requireContext(), "User already registered", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan data pengguna ke PrefManager
        prefManager.saveUser(username, password, email, phone)
        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()

        // Pindah Ke Address Fragment
        val action = RegisterFragmentDirections.actionRegisterFragmentToRegisterAddressFragment()
        findNavController().navigate(action)
    }
}