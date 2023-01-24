package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.adapters.CategoryAdapter
import com.example.myapplication.adapters.RequiredFunctionsForCategoryAdapter
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RequiredFunctionsForCategoryAdapter {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    var categoryAdapter = CategoryAdapter(::categoryItemClickListener, ::isCategorySelected)

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

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { uiState ->
                    categoryAdapter.categories = uiState.categoryNames!!
                }
        }
    }

    private fun initializeItemAdapter() {
        val itemAdapter = ProductAdapter(this)
        binding.itemRecyclerView.adapter = itemAdapter

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { uiState ->
                    itemAdapter.items = uiState.items!!
                }
        }
    }

    private fun setVisibilityOfProgress() {

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { uiState ->
                    when (uiState.loadingStatus) {
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
                    }
                }
        }
    }

    override fun categoryItemClickListener(position: Int) {
        viewModel.categoryItemClickListener(position)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun isCategorySelected(categoryName: String): Boolean{
        return viewModel.isCategorySelected(categoryName)
    }
}