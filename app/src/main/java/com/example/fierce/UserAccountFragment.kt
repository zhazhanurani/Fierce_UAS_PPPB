package com.example.fierce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fierce.adapter.HistoryAdapter
import com.example.fierce.adapter.ShoesAdapter
import com.example.fierce.database.HistoryDao
import com.example.fierce.database.HistoryDatabase
import com.example.fierce.databinding.FragmentUserAccountBinding
import com.example.fierce.model.History
import com.example.fierce.model.Shoes
import com.example.fierce.sharedPref.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserAccountFragment : Fragment() {
    private val binding by lazy {
        FragmentUserAccountBinding.inflate(layoutInflater)
    }

    // Inisiasi Database dan PrefManager
    private lateinit var prefManager: PrefManager
    private lateinit var executorService: ExecutorService
    private lateinit var historyDao: HistoryDao
    private lateinit var historyAdapter : HistoryAdapter
    private val historyList = mutableListOf<History>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisiasi Database
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

        setupUserData()
        setupHistoryRecyclerView()
        fetchHistoryDatabase()

        binding.btnWaFierce.setOnClickListener {
            prefManager.clearUser()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun setupUserData(){
        // Inisiasi PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        // Mengambil data username dan email dari PrefManager
        val username = prefManager.getUsername()
        val email = prefManager.getEmail()
        val address = prefManager.getAddress()

        // Menampilkan data di UI
        with(binding) {
            accUsername.text = username
            accEmail.text = email
            accAdress.text = address
        }
    }

    fun setupHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyList, { shoes ->
            val bundle = Bundle().apply {
                putInt("id", shoes.id)
                putString("brand", shoes.brand)
                putString("status", shoes.status)
                putFloat("size", shoes.size)
                putDouble("price", shoes.price)
                putString("detail", shoes.detail)
                putString("image", shoes.image)
            }
            findNavController().navigate(R.id.action_userAccountFragment_to_fragmentDetails, bundle)
        }, { shoes ->
            deleteHistory(shoes)  // Handle long-click to delete
        })

        binding.HistoryRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = historyAdapter
        }
    }

    fun fetchHistoryDatabase(){
        historyDao.getAllHistory().observe(viewLifecycleOwner){
            history->
            historyList.clear()
            historyList.addAll(history)
            historyAdapter.notifyDataSetChanged()

        }
    }
    // Handle deleting an item from the database
    private fun deleteHistory(shoes: History) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Delete the item from the database
                historyDao.deleteHistory(shoes)

                // Update UI after deletion
                withContext(Dispatchers.Main) {
                    historyList.remove(shoes)
                    historyAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("DeleteHistory", "Error deleting history: ${e.localizedMessage}", e)
            }
        }
    }
}