package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusnote.R
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.databinding.ItemUserBinding
import com.example.nimbusnote.viewModel.FirebaseViewModel

class UserAdapter(
    private val dataset: List<User>,
    private val viewModel: FirebaseViewModel
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.userNameTV.text = item.userName

        holder.binding.userChatCV.setOnClickListener {
            viewModel.setCurrentChat(item.userId)
            holder.itemView.findNavController().navigate(R.id.chatFragment)
        }
    }
}
