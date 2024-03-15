package com.example.nimbusnote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.R
import com.example.nimbusnote.adapter.ChatAdapter
import com.example.nimbusnote.data.model.Chat
import com.example.nimbusnote.databinding.FragmentChatBinding
import com.example.nimbusnote.databinding.FragmentLoginBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment zur Darstellung des Chats.
 *
 * Dieses Fragment ist zuständig für die Darstellung der Chat-Oberfläche und die Interaktion mit dem
 * FirebaseViewModel für das Senden und Empfangen von Nachrichten.
 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    /**
     * Erstellt die Ansicht des Fragments.
     *
     * @param inflater Der LayoutInflater des Fragments.
     * @param container Der Container, in dem das Fragment dargestellt wird.
     * @param savedInstanceState Ein Bundle mit gespeicherten Daten für die Wiederherstellung des Zustands.
     * @return Die Wurzelansicht des Fragments.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    /**
     * Wird aufgerufen, sobald die Ansicht vollständig erstellt wurde.
     *
     * Hier werden die Interaktionen mit der Benutzeroberfläche initialisiert, einschließlich des Sendens von Nachrichten
     * und der Anzeige von Chat-Nachrichten durch das Abonnieren von Änderungen im ViewModel.
     *
     * @param view Die erstellte Ansicht.
     * @param savedInstanceState Ein Bundle mit gespeicherten Daten für die Wiederherstellung des Zustands.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Abonnieren von Änderungen im Chat und Aktualisieren des Adapters.
        viewModel.currentChat.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val chat = value.toObject(Chat::class.java)
                binding.chatRV.adapter = chat?.messages?.let { ChatAdapter(it, viewModel.currentUserId) }
            }
        }

        // Senden einer neuen Nachricht, wenn der Senden-Button geklickt wird.
        binding.sendChatBTN.setOnClickListener {
            val text = binding.messageET.text.toString()
            if (text != "") {
                viewModel.sendNewMessage(text)
                binding.messageET.text?.clear()
            }
        }
    }
}
