package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.Item
import com.example.myapplication.network.NetworkInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    private val _categoryNames = MutableLiveData<MutableList<String>>()
    val categoryNames: LiveData<MutableList<String>> = _categoryNames

    private val _selectedCategory = MutableLiveData(0)
    val selectedCategory: LiveData<Int> = _selectedCategory

    private val _loadingItems = MutableLiveData(true)
    val loadingItems: LiveData<Boolean> = _loadingItems

    val selectedCategoryCardBackgroundColor = "#b495f0"


    fun getAllItemsData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingItems.postValue(true)
                val response = NetworkInstance.api.getAllItemsData()
                if(response.isSuccessful && response.body() != null){
                    _items.postValue(response.body()!!)
                    _loadingItems.postValue(false)
                }
            }
            catch (e: Exception) {
                Log.d("TEST", "Unable to load the data")
            }
        }
    }

    fun getCategoryNamesData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingItems.postValue(true)
                val response = NetworkInstance.api.getCategoriesData()
                if(response.isSuccessful && response.body() != null){
                    _categoryNames.postValue((mutableListOf("all") + response.body()!!) as MutableList<String>?)
                    _loadingItems.postValue(false)
                }
            }
            catch (e: Exception) {
                Log.d("TEST", "Unable to load the data")
            }
        }
    }

    fun getCategoryItemsData(categoryName: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingItems.postValue(true)
                val response = NetworkInstance.api.getCategoryItems(categoryName)
                if(response.isSuccessful && response.body() != null){
                    _items.postValue(response.body()!!)
                    _loadingItems.postValue(false)
                }
            }
            catch (e: Exception) {
                Log.d("TEST", "Unable to load the data")
            }
        }
    }

    fun updateSelectedCategory(selectedCategory: Int){
        _selectedCategory.value = selectedCategory
    }

    fun emptyItemAdapterList(){
        _items.value = listOf()
    }
}