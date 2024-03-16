package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nimbusnote.data.model.Message
import com.example.nimbusnote.databinding.ItemChatInBinding
import com.example.nimbusnote.databinding.ItemChatOutBinding

class ChatAdapter(
    private val dataset: List<Message>,
    private val currentUserId: String
): RecyclerView.Adapter<ViewHolder>() {

    private val inType = 1
    private val outType = 2

    override fun getItemViewType(position: Int): Int {
        val item = dataset[position]
        return if (item.sender == currentUserId) {
            outType
        } else {
            inType
        }
    }

    inner class ChatInViewHolder(val binding: ItemChatInBinding): ViewHolder(binding.root)
    inner class ChatOutViewHolder(val binding: ItemChatOutBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == inType) {
            val binding = ItemChatInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatInViewHolder(binding)
        } else {
            val binding = ItemChatOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatOutViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]

        if (holder is ChatInViewHolder) {
            holder.binding.chatInTV.text = item.text
        } else if (holder is ChatOutViewHolder) {
            holder.binding.chatOutTV.text = item.text
        }

    }

}
