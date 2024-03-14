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

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return (binding.root)
    }

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
            if (text != "") {
                viewModel.sendNewMessage(text)

            }
        }
    }
}
