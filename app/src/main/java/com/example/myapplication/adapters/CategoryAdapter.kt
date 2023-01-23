package com.example.myapplication.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainViewModel
import com.example.myapplication.databinding.ItemCategoryBinding

const val selectedCategoryCardBackgroundColor = "#b495f0"

class CategoryAdapter(private val clickListener: (Int) -> (Unit), private val isCategorySelected: (String) -> (Boolean)) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return isCategorySelected(oldItem) == isCategorySelected(newItem)
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var categories: MutableList<String>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.binding.categoryNameTV.text = categories[position]

        holder.binding.cardView.setOnClickListener {
            clickListener(position)
        }

        val isSelected = isCategorySelected(categories[position])
        val colorStr = if (isSelected) selectedCategoryCardBackgroundColor else "#FFFFFF"
        holder.binding.cardView.setCardBackgroundColor(Color.parseColor(colorStr))
    }

    override fun getItemCount(): Int = categories.size
}



