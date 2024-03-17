package com.example.nimbusnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbusnote.data.model.Note
import com.example.nimbusnote.databinding.ItemNoteBinding
import com.example.nimbusnote.viewModel.MainViewModel

class NoteAdapter(
    private val dataset: List<Note>,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder (val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.noteTV.text = item.text
        holder.binding.noteCV.setOnClickListener {
            viewModel.deleteNote(item)
        }
    }

}
