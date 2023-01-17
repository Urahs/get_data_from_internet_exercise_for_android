package com.example.myapplication

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

    private val _categoryNames = MutableLiveData<List<String>>()
    val categoryNames: LiveData<List<String>> = _categoryNames

    fun getAllItemsData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = NetworkInstance.api.getAllItemsData()
                if(response.isSuccessful && response.body() != null){
                    _items.postValue(response.body()!!)
                }
                else{

                }
            }
            catch (e: Exception) {

            }
        }
    }

    fun getCategoryNamesData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = NetworkInstance.api.getCategoriesData()
                if(response.isSuccessful && response.body() != null){
                    _categoryNames.postValue(response.body()!!)
                }
                else{

                }
            }
            catch (e: Exception) {

            }
        }
    }
}