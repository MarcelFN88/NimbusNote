package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusnote.data.model.Message
import com.example.nimbusnote.databinding.ItemChatInBinding
import com.example.nimbusnote.databinding.ItemChatOutBinding

/**
 * Ein Adapter für den Chat, um Nachrichten in einem RecyclerView anzuzeigen.
 *
 * @param dataset Eine Liste von Nachrichten.
 * @param currentUserId Die ID des aktuellen Benutzers, um eingehende und ausgehende Nachrichten zu unterscheiden.
 */
class ChatAdapter(
    private val dataset: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inType = 1
    private val outType = 2

    /**
     * Bestimmt den Typ der Ansicht basierend auf dem Sender der Nachricht.
     *
     * @param position Die Position der Nachricht in der Liste.
     * @return Ein Integer-Wert, der den Typ der Nachricht darstellt (eingehend oder ausgehend).
     */
    override fun getItemViewType(position: Int): Int {
        val item = dataset[position]
        return if (item.sender == currentUserId) {
            outType
        } else {
            inType
        }
    }

    inner class ChatInViewHolder(val binding: ItemChatInBinding) : RecyclerView.ViewHolder(binding.root)
    inner class ChatOutViewHolder(val binding: ItemChatOutBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt einen neuen ViewHolder basierend auf dem Typ der Ansicht.
     *
     * @param parent Der ViewGroup, in dem die neuen Ansichten angefordert werden.
     * @param viewType Der Typ der neuen Ansicht.
     * @return Ein neuer ViewHolder der entsprechenden Ansicht.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == inType) {
            val binding = ItemChatInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatInViewHolder(binding)
        } else {
            val binding = ItemChatOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatOutViewHolder(binding)
        }
    }

    /**
     * Gibt die Größe des Datasets zurück.
     *
     * @return Die Anzahl der Elemente im Dataset.
     */
    override fun getItemCount(): Int {
        return dataset.size
    }

    /**
     * Bindet die Daten der Nachricht an den ViewHolder.
     *
     * @param holder Der ViewHolder, der aktualisiert werden soll.
     * @param position Die Position der Nachricht in der Liste.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]

        when (holder) {
            is ChatInViewHolder -> holder.binding.chatInTV.text = item.text
            is ChatOutViewHolder -> holder.binding.chatOutTV.text = item.text
        }
    }
}
