package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusnote.data.model.Note
import com.example.nimbusnote.databinding.ItemNoteBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel

/**
 * Adapter für die Notiz-Ansicht, verwaltet die Darstellung von Notizen.
 *
 * @param dataset Liste von Notizen.
 * @param viewModel ViewModel, das für Interaktionen mit Firebase verantwortlich ist.
 */
class NoteAdapter(
    private val dataset: List<Note>,
    private val viewModel: FirebaseViewModel
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt die ViewHolder für Notizen.
     *
     * @param parent Der übergeordnete ViewGroup.
     * @param viewType Der Typ der Ansicht.
     * @return Eine Instanz von NoteViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
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
     * Bindet die Daten der Notiz an die Ansicht und implementiert Klick-Listener zum Löschen.
     *
     * @param holder Der ViewHolder der Ansicht.
     * @param position Die Position der Notiz in der Liste.
     */
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.NoteTV.text = item.text
        holder.binding.noteCV.setOnClickListener {
            viewModel.deleteNote(item)
        }
    }
}
