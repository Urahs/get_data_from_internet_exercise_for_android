package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.adapters.CategoryAdapter
import com.example.myapplication.adapters.CategoryAdapterFunctions
import com.example.myapplication.adapters.ItemAdapter
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CategoryAdapterFunctions {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    var categoryAdapter = CategoryAdapter(::categoryItemClickListener, ::getCategoryBgColor, ::isCategorySelected)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        viewModel.getCategoryNamesData()
        viewModel.handleItemListForConfigChanges()

        initializeItemAdapter()
        initializeCategoryAdapter()
        setVisibilityOfProgress()
    }

    private fun initializeCategoryAdapter() {
        binding.categoryRecyclerView.adapter = categoryAdapter

        viewModel.categoryNames.observe(this){ categoryNameList ->
            categoryAdapter.categories = categoryNameList
            categoryAdapter.notifyDataSetChanged()
        }
    }

    private fun initializeItemAdapter() {
        var itemAdapter = ItemAdapter(this)
        binding.itemRecyclerView.adapter = itemAdapter

        viewModel.items.observe(this){ itemDataList ->
            itemAdapter.items = itemDataList
            itemAdapter.notifyDataSetChanged()
        }
    }

    private fun setVisibilityOfProgress() {
        viewModel.loadingStatus.observe(this){ status ->

            when (status) {
                ApiStatus.LOADING -> {
                    binding.loadingIV.visibility = View.VISIBLE
                    binding.itemRecyclerView.visibility = View.INVISIBLE
                    binding.loadingIV.setImageResource(R.drawable.loading_animation)
                }
                ApiStatus.ERROR -> {
                    binding.loadingIV.visibility = View.VISIBLE
                    binding.itemRecyclerView.visibility = View.INVISIBLE
                    binding.loadingIV.setImageResource(R.drawable.ic_connection_error)
                }
                ApiStatus.DONE -> {
                    binding.itemRecyclerView.visibility = View.VISIBLE
                    binding.loadingIV.visibility = View.INVISIBLE
                }
                else -> {}
            }
        }
    }

    override fun categoryItemClickListener(position: Int) {
        viewModel.categoryItemClickListener(position)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun getCategoryBgColor(position: Int): Int {
        return viewModel.getCategoryBgColor(position)
    }

    fun isCategorySelected(position: String): Boolean{
        return viewModel.categoryNames.value!![viewModel.selectedCategory.value!!] == position
    }
}