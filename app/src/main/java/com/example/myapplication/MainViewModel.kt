package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.Product
import com.example.myapplication.network.NetworkInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ApiStatus {
    LOADING,
    ERROR,
    DONE
}

data class AppUiState(
    var items: List<Product> = listOf(),
    var categoryNames: MutableList<String>? = mutableListOf(),
    var selectedCategory: Int = 0,
    var loadingStatus: ApiStatus = ApiStatus.LOADING
)

class MainViewModel: ViewModel() {

    /*
    private val _items = MutableLiveData<List<Product>>()
    val items: LiveData<List<Product>> = _items

    private val _categoryNames = MutableLiveData<MutableList<String>>()
    val categoryNames: LiveData<MutableList<String>> = _categoryNames

    private val _selectedCategory = MutableLiveData(0)
    val selectedCategory: LiveData<Int> = _selectedCategory

    private val _loadingStatus = MutableLiveData(ApiStatus.LOADING)
    val loadingStatus: LiveData<ApiStatus> = _loadingStatus
     */

    private val _uiState = MutableLiveData(AppUiState())
    val uiState: LiveData<AppUiState> = _uiState


    fun getAllItemsData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("TEST", "111")
                _uiState.value!!.loadingStatus = ApiStatus.LOADING
                Log.d("TEST", "222")
                //_loadingStatus.postValue(ApiStatus.LOADING)
                val response = NetworkInstance.api.getAllItemsData()
                Log.d("TEST", "333")
                if(response.isSuccessful && response.body() != null){
                    //_uiState.value = AppUiState(items = response.body()!!)
                    Log.d("TEST", "444")
                    _uiState.value!!.items = response.body()!!
                    Log.d("TEST", "555 + ${_uiState.value!!.items[0].category}")
                    _uiState.value!!.loadingStatus = ApiStatus.DONE
                    //_items.postValue(response.body()!!)
                    //_loadingStatus.postValue(ApiStatus.DONE)
                    Log.d("TEST", "OOOOOOOOOO")
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
                _uiState.value!!.loadingStatus = ApiStatus.LOADING
                //_loadingStatus.postValue(ApiStatus.LOADING)
                val response = NetworkInstance.api.getCategoriesData()
                if(response.isSuccessful && response.body() != null){
                    _uiState.value!!.categoryNames = (mutableListOf("all") + response.body()!!) as MutableList<String>?
                    Log.d("TEST", "999 + ${_uiState.value!!.categoryNames}")
                    _uiState.value!!.loadingStatus = ApiStatus.DONE
                    Log.d("TEST", "OOOOOOOOOO")
                    //_categoryNames.postValue((mutableListOf("all") + response.body()!!) as MutableList<String>?)
                    //_loadingStatus.postValue(ApiStatus.DONE)
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
                _uiState.value!!.loadingStatus = ApiStatus.LOADING
                //_loadingStatus.postValue(ApiStatus.LOADING)
                val response = NetworkInstance.api.getCategoryItems(categoryName)
                if(response.isSuccessful && response.body() != null){
                    //_items.postValue(response.body()!!)
                    //_loadingStatus.postValue(ApiStatus.DONE)
                    _uiState.value!!.items = response.body()!!
                    _uiState.value!!.loadingStatus = ApiStatus.DONE
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
        //_loadingStatus.postValue(ApiStatus.ERROR)
        _uiState.value!!.loadingStatus = ApiStatus.ERROR
    }

    fun updateSelectedCategory(selectedCategory: Int){
        _uiState.value!!.selectedCategory = selectedCategory
        //_selectedCategory.value = selectedCategory

    }

    fun emptyItemAdapterList(){
        //_items.value = listOf()
        _uiState.value!!.items = listOf()
    }

    fun handleItemListForConfigChanges(){
        if(_uiState.value!!.selectedCategory == 0)
            getAllItemsData()
        else
            getCategoryItemsData(_uiState.value!!.categoryNames!![_uiState.value!!.selectedCategory])
    }

    fun categoryItemClickListener(position: Int) {
        if(_uiState.value!!.selectedCategory != position){

            emptyItemAdapterList()

            if(position == 0)
                getAllItemsData()
            else
                getCategoryItemsData(_uiState.value!!.categoryNames!![position])

            updateSelectedCategory(position)
        }
    }

    fun isCategorySelected(category: String): Boolean{
        return _uiState.value!!.categoryNames!![_uiState.value!!.selectedCategory] == category
    }
}