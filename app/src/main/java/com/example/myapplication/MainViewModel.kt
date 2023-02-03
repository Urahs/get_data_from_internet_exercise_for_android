package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.FakeStoreApiService
import com.example.myapplication.network.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ApiStatus {
    LOADING,
    ERROR,
    DONE
}

data class AppUiState(
    var items: List<Product> = listOf(),
    var categoryNames: MutableList<String> = mutableListOf(),
    var selectedCategory: Int = 0,
    var loadingStatus: ApiStatus = ApiStatus.LOADING
)

@HiltViewModel
class MainViewModel @Inject constructor (
    private val apiService: FakeStoreApiService
): ViewModel() {

    private val _uiState = MutableLiveData(AppUiState())
    val uiState: LiveData<AppUiState> = _uiState


    fun getAllItemsData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.postValue(
                    _uiState.value!!.copy(
                        loadingStatus = ApiStatus.LOADING
                    )
                )
                val response = apiService.getAllItemsData()
                if(response.isSuccessful && response.body() != null){
                    _uiState.postValue(
                        _uiState.value!!.copy(
                            items = response.body()!!,
                            loadingStatus = ApiStatus.DONE
                        )
                    )
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
                _uiState.postValue(
                    _uiState.value!!.copy(
                        loadingStatus = ApiStatus.LOADING
                    )
                )
                val response = apiService.getCategoriesData()
                if(response.isSuccessful && response.body() != null){
                    _uiState.postValue(
                        _uiState.value!!.copy(
                            categoryNames = (mutableListOf("all") + response.body()!!) as MutableList<String>,
                            loadingStatus = ApiStatus.DONE
                        )
                    )
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
                _uiState.postValue(
                    _uiState.value!!.copy(
                        loadingStatus = ApiStatus.LOADING
                    )
                )
                val response = apiService.getCategoryItems(categoryName)
                if(response.isSuccessful && response.body() != null){
                    _uiState.postValue(
                        _uiState.value!!.copy(
                            items = response.body()!!,
                            loadingStatus = ApiStatus.DONE
                        )
                    )
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
        _uiState.postValue(
            _uiState.value!!.copy(
                loadingStatus = ApiStatus.ERROR
            )
        )
    }

    fun updateSelectedCategory(selectedCategory: Int){
        _uiState.value!!.selectedCategory = selectedCategory
    }

    fun emptyItemAdapterList(){
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