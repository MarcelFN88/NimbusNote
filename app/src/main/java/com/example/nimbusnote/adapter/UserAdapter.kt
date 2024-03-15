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

/**
 * Adapter für die Benutzeransicht, verwaltet die Darstellung von Benutzerinformationen.
 *
 * @param dataset Liste von Benutzern.
 * @param viewModel ViewModel, das für Interaktionen mit Firebase und die Navigation verantwortlich ist.
 */
class UserAdapter(
    private val dataset: List<User>,
    private val viewModel: FirebaseViewModel
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    /**
     * ViewHolder für Benutzer, hält die Ansicht und die Datenbindungen.
     */
    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt die ViewHolder für Benutzer.
     *
     * @param parent Der übergeordnete ViewGroup.
     * @param viewType Der Typ der Ansicht.
     * @return Eine Instanz von UserViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    /**
     * Gibt die Größe des Datensatzes zurück.
     *
     * @return Die Größe des Datensatzes.
     */
    override fun getItemCount(): Int {
        return dataset.size
    }

    /**
     * Bindet die Daten des Benutzers an die Ansicht. Setzt den Benutzernamen, lädt das Profilbild
     * und implementiert einen Klick-Listener für die Profilbildansicht, um den Chat zu öffnen.
     *
     * @param holder Der ViewHolder der Ansicht.
     * @param position Die Position des Benutzers in der Liste.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.userNameTV.text = item.userName

        Glide.with(holder.itemView.context)
            .load(item.userImage)
            .apply(RequestOptions().circleCrop())
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(holder.binding.userProfileImageView)

        holder.binding.userProfileImageView.setOnClickListener {
            viewModel.setCurrentChat(item.userId)
            holder.itemView.findNavController().navigate(R.id.chatFragment)
        }
    }
}
