package com.example.nimbusnote.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.adapter.ChatAdapter
import com.example.nimbusnote.data.model.Chat
import com.example.nimbusnote.databinding.FragmentChatBinding
import com.example.nimbusnote.viewModel.MainViewModel

/**
 * Fragment für den Chat-Bildschirm.
 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Erstellt die Ansicht des Fragments.
     *
     * @param inflater Der LayoutInflater, der zum Aufblasen des Layouts verwendet wird.
     * @param container Die ViewGroup, in die das Fragment-Layout eingefügt wird.
     * @param savedInstanceState Das gespeicherte Instanzzustands-Bundle.
     * @return Die aufgeblasene View des Fragments.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    /**
     * Wird aufgerufen, nachdem die Ansicht des Fragments erstellt wurde.
     *
     * @param view Die aufgeblasene Ansicht des Fragments.
     * @param savedInstanceState Das gespeicherte Instanzzustands-Bundle.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentChat.addSnapshotListener { value, error ->
            if (error == null && value!= null) {
                val chat = value.toObject(Chat::class.java)
                binding.chatRV.adapter = chat?.messages?.let { ChatAdapter(it, viewModel.currentUserId) }
            }
        }

        binding.sendChatBTN.setOnClickListener {
            val text = binding.messageET.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendNewMessage(text)
                binding.messageET.setText("")
            }
        }
    }
}
