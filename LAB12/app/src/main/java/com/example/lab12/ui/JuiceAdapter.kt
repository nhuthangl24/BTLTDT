package com.example.lab12.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab12.data.Juice
import com.example.lab12.databinding.ItemJuiceBinding

class JuiceAdapter(
    private val onItemClick: (Juice) -> Unit = {}
) : ListAdapter<Juice, JuiceAdapter.JuiceViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Juice>() {
        override fun areItemsTheSame(oldItem: Juice, newItem: Juice): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Juice, newItem: Juice): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuiceViewHolder {
        val binding = ItemJuiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JuiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JuiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JuiceViewHolder(private val binding: ItemJuiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(juice: Juice) {
            binding.juiceName.text = juice.name
            binding.juiceDescription.text = juice.description
            binding.colorCircle.backgroundTintList = ColorStateList.valueOf(juice.color.colorInt)
            binding.juiceRatingText.text = "★ ${juice.rating}/5"

            binding.root.setOnClickListener {
                onItemClick(juice)
            }
        }
    }
}
