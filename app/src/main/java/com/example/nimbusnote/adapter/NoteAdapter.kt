package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusnote.data.model.Note
import com.example.nimbusnote.databinding.ItemNoteBinding
import com.example.nimbusnote.viewModel.MainViewModel

/**
 * Adapter zur Verwaltung von Notizen in einem RecyclerView.
 *
 * @property dataset Liste von Note-Objekten, die die Notizen repräsentieren.
 * @property viewModel MainViewModel-Objekt für die Interaktion mit den Notizen.
 */
class NoteAdapter(
    private val dataset: List<Note>,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    /**
     * ViewHolder für Notizen.
     *
     * @param binding ItemNoteBinding-Objekt zum Zugriff auf Ansichten.
     */
    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt einen neuen ViewHolder, wenn dies erforderlich ist.
     *
     * @param parent Die ViewGroup, in die die neue Ansicht eingefügt wird.
     * @param viewType Der Ansichtstyp der neuen Ansicht.
     * @return NoteViewHolder mit dem aufgeblasenen Layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
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
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.noteTV.text = item.text
        holder.binding.noteCV.setOnClickListener {
            viewModel.deleteNote(item)
        }
    }
}
