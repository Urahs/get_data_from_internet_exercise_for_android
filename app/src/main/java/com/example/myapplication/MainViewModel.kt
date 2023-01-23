package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.Product
import com.example.myapplication.network.NetworkInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class ApiStatus {
    LOADING,
    ERROR,
    DONE
}

class MainViewModel: ViewModel() {

    private val _items = MutableLiveData<List<Product>>()
    val items: LiveData<List<Product>> = _items

    private val _categoryNames = MutableLiveData<MutableList<String>>()
    val categoryNames: LiveData<MutableList<String>> = _categoryNames

    private val _selectedCategory = MutableLiveData(0)
    val selectedCategory: LiveData<Int> = _selectedCategory

    private val _loadingStatus = MutableLiveData(ApiStatus.LOADING)
    val loadingStatus: LiveData<ApiStatus> = _loadingStatus


    fun getAllItemsData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingStatus.postValue(ApiStatus.LOADING)
                val response = NetworkInstance.api.getAllItemsData()
                if(response.isSuccessful && response.body() != null){
                    _items.postValue(response.body()!!)
                    _loadingStatus.postValue(ApiStatus.DONE)
                }
                else
                    errorHandler()
            }
            catch (e: Exception) {
                errorHandler()
            }
        }
    }

    fun getCategoryNamesData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingStatus.postValue(ApiStatus.LOADING)
                val response = NetworkInstance.api.getCategoriesData()
                if(response.isSuccessful && response.body() != null){
                    _categoryNames.postValue((mutableListOf("all") + response.body()!!) as MutableList<String>?)
                    _loadingStatus.postValue(ApiStatus.DONE)
                }
                else
                    errorHandler()
            }
            catch (e: Exception) {
                errorHandler()
            }
        }
    }

    fun getCategoryItemsData(categoryName: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingStatus.postValue(ApiStatus.LOADING)
                val response = NetworkInstance.api.getCategoryItems(categoryName)
                if(response.isSuccessful && response.body() != null){
                    _items.postValue(response.body()!!)
                    _loadingStatus.postValue(ApiStatus.DONE)
                }
                else
                    errorHandler()
            }
            catch (e: Exception) {
                errorHandler()
            }
        }
    }

    private fun errorHandler() {
        Log.d("TEST", "Unable to load the data")
        _loadingStatus.postValue(ApiStatus.ERROR)
    }

    fun updateSelectedCategory(selectedCategory: Int){
        _selectedCategory.value = selectedCategory
    }

    fun emptyItemAdapterList(){
        _items.value = listOf()
    }

    fun handleItemListForConfigChanges(){
        if(selectedCategory.value == 0)
            getAllItemsData()
        else
            getCategoryItemsData(categoryNames.value!!.get(selectedCategory.value!!))
    }

    fun categoryItemClickListener(position: Int) {
        if(selectedCategory.value != position){

            emptyItemAdapterList()

            if(position == 0)
                getAllItemsData()
            else
                getCategoryItemsData(categoryNames.value!![position])

            updateSelectedCategory(position)
        }
    }

    fun isCategorySelected(category: String): Boolean{
        return categoryNames.value!![selectedCategory.value!!] == category
    }
}