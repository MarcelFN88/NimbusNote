package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nimbusnote.R
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.databinding.ItemUserBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel

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

        // Lade das Profilbild mit Glide
// Innerhalb der onBindViewHolder Methode deines UserAdapters
        Glide.with(holder.itemView.context)
            .load(item.userImage) // URL des Profilbilds
            .apply(RequestOptions().circleCrop()) // Macht das Bild rund
            .placeholder(R.drawable.ic_profile_placeholder) // Platzhalter
            .into(holder.binding.userProfileImageView)

        holder.binding.userProfileImageView.setOnClickListener {
            viewModel.setCurrentChat(item.userId)
            holder.itemView.findNavController().navigate(R.id.chatFragment)
        }
    }
}
