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
                    Log.d("TEST", "ELSEEEE")
                }
            }
            catch (e: Exception) {
                Log.d("TEST", "CATCHHHH")
            }
        }
    }

    fun getCategoryItemsData(categoryName: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = NetworkInstance.api.getCategoryItems(categoryName)
                if(response.isSuccessful && response.body() != null){
                    _items.postValue(response.body()!!)
                }
                else{
                    Log.d("TEST", "ELSEEEE")
                }
            }
            catch (e: Exception) {
                Log.d("TEST", "CATCHHHH")
            }
        }
    }
}