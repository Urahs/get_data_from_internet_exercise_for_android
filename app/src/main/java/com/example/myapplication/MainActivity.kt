package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getAllItemsData()
        viewModel.getCategoryNamesData()

        initializeItemAdapter()
        initializeCategoryAdapter()
    }

    private fun initializeCategoryAdapter() {
        var categoryAdapter = CategoryAdapter(this)
        binding.categoryRecyclerView.adapter = categoryAdapter

        viewModel.categoryNames.observe(this){ categoryNameList ->
            categoryAdapter!!.categories = categoryNameList
            categoryAdapter!!.notifyDataSetChanged()
        }
    }

    private fun initializeItemAdapter() {
        var itemAdapter = ItemAdapter(this)
        binding.itemRecyclerView.adapter = itemAdapter

        viewModel.items.observe(this){ itemDataList ->
            itemAdapter!!.items = itemDataList
            itemAdapter!!.notifyDataSetChanged()
        }
    }

    /*
    private fun showDialog(item: Item) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Item Details")
        builder.setMessage("Name: ${item.title}\nDescription: ${item.description}")
        builder.setPositiveButton("OK") { _, _ ->
            // Perform any action when OK button is clicked
        }
        val dialog = builder.create()
        dialog.show()
    }
     */
}