package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ItemShoppingItemBinding
import com.example.myapplication.network.NetworkInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var itemAdapter = ItemAdapter(this)


        lifecycleScope.launch(Dispatchers.IO){

            try {
                val response = NetworkInstance.api.getItemData()
                withContext(Dispatchers.Main){
                    binding.itemRecyclerView.adapter = itemAdapter
                    itemAdapter!!.items = response
                    itemAdapter!!.notifyDataSetChanged()
                }
            }
            catch (e: Exception) {
                Log.d("TEST", "CRASHHHHHHHH!!!!")
            }

        }


    }


}