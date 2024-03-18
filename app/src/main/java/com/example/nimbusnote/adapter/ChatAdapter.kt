package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nimbusnote.data.model.Message
import com.example.nimbusnote.databinding.ItemChatInBinding
import com.example.nimbusnote.databinding.ItemChatOutBinding

/**
 * Adapter zur Verwaltung von Chat-Nachrichten in einem RecyclerView.
 *
 * @property dataset Liste von Message-Objekten, die die Chat-Nachrichten repräsentieren.
 * @property currentUserId Kennung für den aktuellen Benutzer.
 */
class ChatAdapter(
    private val dataset: List<Message>,
    private val currentUserId: String
): RecyclerView.Adapter<ViewHolder>() {

    private val inType = 1
    private val outType = 2

    /**
     * Bestimmt den Typ der Ansicht für eine bestimmte Position im Datensatz.
     *
     * @param position Die Position des Elements im Datensatz.
     * @return Int, der den Typ der Ansicht darstellt.
     */
    override fun getItemViewType(position: Int): Int {
        val item = dataset[position]
        return if (item.sender == currentUserId) {
            outType
        } else {
            inType
        }
    }

    /**
     * ViewHolder für eingehende Chat-Nachrichten.
     *
     * @param binding ItemChatInBinding-Objekt zum Zugriff auf Ansichten.
     */
    inner class ChatInViewHolder(val binding: ItemChatInBinding): ViewHolder(binding.root)

    /**
     * ViewHolder für ausgehende Chat-Nachrichten.
     *
     * @param binding ItemChatOutBinding-Objekt zum Zugriff auf Ansichten.
     */
    inner class ChatOutViewHolder(val binding: ItemChatOutBinding): ViewHolder(binding.root)

    /**
     * Inflates das entsprechende Layout für den ViewHolder basierend auf dem Ansichtstyp.
     *
     * @param parent Die ViewGroup, in die die neue Ansicht eingefügt wird.
     * @param viewType Der Ansichtstyp der neuen Ansicht.
     * @return ViewHolder mit dem aufgeblasenen Layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == inType) {
            val binding = ItemChatInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatInViewHolder(binding)
        } else {
            val binding = ItemChatOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatOutViewHolder(binding)
        }
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]

        if (holder is ChatInViewHolder) {
            holder.binding.chatInTV.text = item.text
        } else if (holder is ChatOutViewHolder) {
            holder.binding.chatOutTV.text = item.text
        }
    }
}
