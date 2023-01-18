package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCategoryBinding

class CategoryAdapter(private val activity: AppCompatActivity) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
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

        val viewModel: MainViewModel by lazy {
            ViewModelProvider(activity).get(MainViewModel::class.java)
        }

        holder.binding.categoryNameTV.text = categories[position]

        holder.binding.cardView.setOnClickListener {
            if(viewModel.selectedCategory.value != position){

                viewModel.emptyItemAdapterList()

                if(position == 0)
                    viewModel.getAllItemsData()
                else
                    viewModel.getCategoryItemsData(categories[position])

                viewModel.updateSelectedCategory(position)
            }
        }

        viewModel.selectedCategory.observe(activity){
            if(position == viewModel.selectedCategory.value)
                holder.binding.cardView.setCardBackgroundColor(Color.parseColor(viewModel.selectedCategoryCardBackgroundColor))
            else
                holder.binding.cardView.setCardBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int = categories.size
}



