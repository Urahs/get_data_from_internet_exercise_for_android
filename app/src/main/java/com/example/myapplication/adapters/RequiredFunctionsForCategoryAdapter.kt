package com.example.myapplication.adapters

interface RequiredFunctionsForCategoryAdapter {
    fun categoryItemClickListener(position: Int)
    fun isCategorySelected(position: String) : Boolean
}