package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        viewModel.getAllItemsData()
        viewModel.getCategoryNamesData()

        initializeItemAdapter()
        initializeCategoryAdapter()

        viewModel.loadingItems.observe(this){
            binding.loadingTV.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
            binding.itemRecyclerView.visibility = if (it == false) View.VISIBLE else View.INVISIBLE
        }


    }

    private fun initializeCategoryAdapter() {
        var categoryAdapter = CategoryAdapter(this)
        binding.categoryRecyclerView.adapter = categoryAdapter

        viewModel.categoryNames.observe(this){ categoryNameList ->
            categoryNameList.add(0, "all")
            viewModel.updateSelectedCategory(0)
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
}