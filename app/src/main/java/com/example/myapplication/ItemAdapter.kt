package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.provider.Settings.Secure.getString
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        //holder.binding.statusImage.setImageResource(R.drawable.loading_animation)

        holder.binding.apply {
            val item = items[position]
            var spannableString: SpannableString


            cardView.setOnClickListener{
                showDialog(item)
            }

            // title
            if(item.title.length > 20)
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
            spannableString = SpannableString(String.format(
                context.getString(R.string.item_price),
                item.price,
                context.getString(R.string.money_type_dollar)))
            priceTV.text = boldIntroductionOfText(spannableString, "price:".length)
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


    private fun showDialog(item: Item) {

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



