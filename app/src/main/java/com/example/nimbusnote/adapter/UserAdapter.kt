package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nimbusnote.R
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.databinding.ItemUserBinding
import com.example.nimbusnote.viewModel.MainViewModel

/**
 * Adapter zur Verwaltung von Benutzern in einem RecyclerView.
 *
 * @property dataset Liste von User-Objekten, die die Benutzer repräsentieren.
 * @property viewModel MainViewModel-Objekt für die Interaktion mit den Benutzern.
 */
class UserAdapter(
    private var dataset: List<User>,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    /**
     * ViewHolder für Benutzer.
     *
     * @param binding ItemUserBinding-Objekt zum Zugriff auf Ansichten.
     */
    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt einen neuen ViewHolder, wenn dies erforderlich ist.
     *
     * @param parent Die ViewGroup, in die die neue Ansicht eingefügt wird.
     * @param viewType Der Ansichtstyp der neuen Ansicht.
     * @return UserViewHolder mit dem aufgeblasenen Layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    /**
     * Gibt die Gesamtzahl der Elemente im Datensatz zurück.
     *
     * @return Int, der die Gesamtzahl der Elemente darstellt.
     */
    override fun getItemCount(): Int {
        return dataset.size
    }

    /**
     * Bindet die Daten an den ViewHolder an der angegebenen Position.
     *
     * @param holder Der ViewHolder, an den die Daten gebunden werden sollen.
     * @param position Die Position des Elements im Datensatz.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.userNameTV.text = item.userName

        if (item.userImage.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(item.userImage)
                .into(holder.binding.userImageView)
        }

        holder.binding.userChatCV.setOnClickListener {
            viewModel.setCurrentChat(item.userId)
            holder.itemView.findNavController().navigate(R.id.chatFragment)
        }
    }

    /**
     * Aktualisiert die Benutzerliste mit einer neuen Liste.
     *
     * @param newList Die neue Liste von Benutzern.
     */
    fun updateUserList(newList: List<User>) {
        dataset = newList
        notifyDataSetChanged()
    }
}
