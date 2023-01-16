package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Secure.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemShoppingItemBinding
import com.example.myapplication.network.Item


class ItemAdapter(private val context: Context) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    class ItemViewHolder(val binding: ItemShoppingItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var items: List<Item>
        get() = differ.currentList
        set(value) {differ.submitList(value)}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemShoppingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]

            // title
            titleTV.text = item.title

            // image
            Glide.with(context)
                .load(item.image)
                .fitCenter()
                .override(itemImageView.width, itemImageView.height)
                .into(itemImageView)

            // description
            itemDescriptionTV.text =  context.getString(R.string.item_description, item.description)

            // price
            priceTV.text = String.format(
                context.getString(R.string.item_price),
                item.price,
                context.getString(R.string.money_type_dollar))

            // rating
            ratingTV.text = String.format(
                context.getString(R.string.item_rating),
                item.rating.rate,
                item.rating.count)
        }
    }

    override fun getItemCount(): Int = items.size
}

