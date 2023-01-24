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
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()


    fun getAllItemsData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update {
                    it.copy(loadingStatus = ApiStatus.LOADING)
                }
                val response = NetworkInstance.api.getAllItemsData()
                if(response.isSuccessful && response.body() != null){
                    _uiState.update {
                        it.copy(
                            items = response.body()!!,
                            loadingStatus = ApiStatus.DONE
                        )
                    }
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
                _uiState.update {
                    it.copy(loadingStatus = ApiStatus.LOADING)
                }
                val response = NetworkInstance.api.getCategoriesData()
                if(response.isSuccessful && response.body() != null){
                    _uiState.update {
                        it.copy(
                            categoryNames = (mutableListOf("all") + response.body()!!) as MutableList<String>?,
                            loadingStatus = ApiStatus.DONE
                        )
                    }
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
                _uiState.update {
                    it.copy(loadingStatus = ApiStatus.LOADING)
                }
                val response = NetworkInstance.api.getCategoryItems(categoryName)
                if(response.isSuccessful && response.body() != null){
                    _uiState.update {
                        it.copy(
                            items = response.body()!!,
                            loadingStatus = ApiStatus.DONE
                        )
                    }
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
        _uiState.value.loadingStatus = ApiStatus.ERROR
    }

    fun updateSelectedCategory(selectedCategory: Int){
        _uiState.value.selectedCategory = selectedCategory
    }

    fun emptyItemAdapterList(){
        _uiState.value.items = listOf()
    }

    fun handleItemListForConfigChanges(){
        if(_uiState.value.selectedCategory == 0)
            getAllItemsData()
        else
            getCategoryItemsData(_uiState.value.categoryNames!![_uiState.value.selectedCategory])
    }

    fun categoryItemClickListener(position: Int) {
        if(_uiState.value.selectedCategory != position){

            emptyItemAdapterList()

            if(position == 0)
                getAllItemsData()
            else
                getCategoryItemsData(_uiState.value.categoryNames!![position])

            updateSelectedCategory(position)
        }
    }

    fun isCategorySelected(category: String): Boolean{
        return _uiState.value!!.categoryNames!![_uiState.value!!.selectedCategory] == category
    }
}