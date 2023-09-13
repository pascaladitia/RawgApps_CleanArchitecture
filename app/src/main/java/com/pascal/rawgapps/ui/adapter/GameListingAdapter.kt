package com.pascal.rawgapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pascal.rawgapps.databinding.GameCardBinding
import com.pascal.rawgapps.domain.model.UIModelListing

class GameListingAdapter(val itemClick: (position: Int) -> Unit) :
    ListAdapter<UIModelListing, GameListingAdapter.UIModelListingViewHolder>(
        UIModelListingDiffCallback()
    ) {
    class UIModelListingViewHolder(private val binding: GameCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(game: UIModelListing) {
            binding.TextViewGameCardName.text = game.name
            Glide.with(itemView.context).load(game.backgroundImage)
                .into(binding.ImageViewGameCardImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UIModelListingViewHolder {
        val binding = GameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UIModelListingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UIModelListingViewHolder,
        position: Int
    ) {
        holder.bindData(getItem(position))
        holder.itemView.setOnClickListener {
            itemClick(position)
        }
    }
}

class UIModelListingDiffCallback : DiffUtil.ItemCallback<UIModelListing>() {
    override fun areItemsTheSame(oldItem: UIModelListing, newItem: UIModelListing): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIModelListing, newItem: UIModelListing): Boolean {
        return oldItem == newItem
    }
}
