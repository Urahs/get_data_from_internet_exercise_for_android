package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.NetworkInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    fun getItemData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = NetworkInstance.api.getItemData()
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
}