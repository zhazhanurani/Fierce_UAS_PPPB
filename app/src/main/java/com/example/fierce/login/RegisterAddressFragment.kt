package com.example.fierce.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fierce.R
import com.example.fierce.databinding.FragmentRegisterAddressBinding
import com.example.fierce.sharedPref.PrefManager

class RegisterAddressFragment : Fragment() {

    private val binding by lazy{
        FragmentRegisterAddressBinding.inflate(layoutInflater)
    }

    private lateinit var prefManager: PrefManager
    private lateinit var provinsi: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provinsi = resources.getStringArray(R.array.provinsi)
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

        val adapterProvinsi = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, provinsi)
        binding.spinnerTujuan.adapter = adapterProvinsi

        // Inisiasi Fragment
        prefManager = PrefManager.getInstance(requireContext())

        with(binding){
            btnLogin.setOnClickListener {
                saveUserAddress()
            }
            menujuLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerAddressFragment_to_loginFragment)
            }
        }
    }

    fun saveUserAddress(){
        val address = binding.registerAddressFill.text.toString()
        val spinnerTujuan = binding.spinnerTujuan.selectedItem.toString()

        if (address.isEmpty() || spinnerTujuan.isEmpty()){
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        prefManager.saveAddress(address,spinnerTujuan)
        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_registerAddressFragment_to_mainActivity)
    }
}