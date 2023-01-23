package com.example.myapplication.adapters

interface CategoryAdapterFunctions {
    fun categoryItemClickListener(position: Int)
    fun getCategoryBgColor(position: Int) : Int
}