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
import com.example.nimbusnote.viewModel.MainViewModel

/**
 * Fragment für die Anzeige und das Hinzufügen von Notizen.
 */
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding // ViewBinding für das Layout dieses Fragments
    private val viewModel: MainViewModel by activityViewModels() // ViewModel für Firebase-Operationen

    /**
     * Wird aufgerufen, um das Layout des Fragments zu inflatieren.
     *
     * @param inflater Der LayoutInflater, der das Layout inflatieren kann.
     * @param container Der Container, in den das Layout eingefügt wird.
     * @param savedInstanceState Ein Bundle mit gespeicherten Zustandsdaten.
     * @return Die View für das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View vollständig erstellt wurde. Hier werden Event-Listener registriert und Daten geladen.
     *
     * @param view Die erstellte View des Fragments.
     * @param savedInstanceState Ein Bundle mit gespeicherten Zustandsdaten.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hört auf Änderungen in der Notiz-Datenbank und aktualisiert das RecyclerView, wenn Änderungen festgestellt werden.
        viewModel.notesRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val myNoteList = value.toObjects(Note::class.java)
                binding.noteRV.adapter = NoteAdapter(myNoteList, viewModel)
            }
        }

        // Fügt eine neue Notiz hinzu, wenn der "Notiz speichern"-Button geklickt wird.
        binding.saveNoteBTN.setOnClickListener {
            val text = binding.noteET.text.toString()
            if (text.isNotEmpty()) {
                viewModel.saveNote(Note(text = text))
                binding.noteET.setText("") // Leert das Eingabefeld nach dem Speichern
            }
        }
    }
}
