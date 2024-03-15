package com.example.nimbusnote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.adapter.NoteAdapter
import com.example.nimbusnote.data.model.Note
import com.example.nimbusnote.databinding.FragmentNoteBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel

/**
 * Fragment zur Darstellung und zum Hinzufügen von Notizen.
 * Erlaubt Benutzern, neue Notizen zu erstellen und vorhandene zu betrachten.
 */
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lauscht auf Änderungen in der Liste der Notizen und aktualisiert die Anzeige entsprechend.
        viewModel.notesRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val myNoteList = value.toObjects(Note::class.java)
                binding.noteRV.adapter = NoteAdapter(myNoteList, viewModel)
            }
        }

        // Fügt eine neue Notiz zur Datenbank hinzu, wenn der Benutzer den Speichern-Button betätigt.
        binding.saveNoteBTN.setOnClickListener {
            val text = binding.noteET.text.toString()
            if (text.isNotEmpty()) {
                viewModel.saveNote(Note(text = text))
                binding.noteET.setText("") // Setzt das Eingabefeld nach dem Speichern zurück
            }
        }
    }
}
