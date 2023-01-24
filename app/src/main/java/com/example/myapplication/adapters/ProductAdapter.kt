package com.example.myapplication.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemShoppingProductBinding
import com.example.myapplication.network.Product


class ProductAdapter(private val context: Context) : RecyclerView.Adapter<ProductAdapter.ItemViewHolder>(){

    class ItemViewHolder(val binding: ItemShoppingProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var items: List<Product>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemShoppingProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val item = items[position]

            cardView.setOnClickListener{
                showDialog(item)
            }

            // title
            if(item.title.length > 21)
                titleTV.text = item.title.take(18) + "..."
            else
                titleTV.text = item.title

            // image
            Glide.with(context)
                .load(item.image)
                .fitCenter()
                .override(itemImageView.width, itemImageView.height)
                .into(itemImageView)

            // price
            priceTV.text = item.price.toString() + context.getString(R.string.money_type_dollar)
        }
    }

    private fun boldIntroductionOfText(spannableString: SpannableString, endIndex: Int): SpannableString{
        val styleSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(
            styleSpan,
            0,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }

    private fun showDialog(item: Product) {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_details_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setTitle("Item Details")

        val titleTV = dialog.findViewById<TextView>(R.id.titleTV)
        val itemDescriptionTV = dialog.findViewById<TextView>(R.id.itemDescriptionTV)
        val ratingTV = dialog.findViewById<TextView>(R.id.ratingTV)
        val priceTV = dialog.findViewById<TextView>(R.id.priceTV)
        val imageView = dialog.findViewById<ImageView>(R.id.itemImageView)
        var spannableString: SpannableString

        // title
        titleTV.text = item.title

        // description
        spannableString = SpannableString(context.getString(R.string.item_description, item.description))
        itemDescriptionTV.text = boldIntroductionOfText(spannableString, "description:".length)

        // image
        Glide.with(context)
            .load(item.image)
            .fitCenter()
            .override(imageView.width, imageView.height)
            .into(imageView)

        // price
        spannableString = SpannableString(String.format(
            context.getString(R.string.item_price),
            item.price,
            context.getString(R.string.money_type_dollar)))
        priceTV.text = boldIntroductionOfText(spannableString, "price:".length)

        // rating
        spannableString = SpannableString(String.format(
            context.getString(R.string.item_rating),
            item.rating.rate,
            item.rating.count))
        ratingTV.text = boldIntroductionOfText(spannableString, "rating:".length)

        dialog.show()
    }

    override fun getItemCount(): Int = items.size
}



