package com.example.nimbusnote.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Repräsentiert einen Chat, der eine Liste von Nachrichten beinhaltet.
 *
 * @property chatId Die eindeutige ID des Chats, automatisch von Firebase Firestore zugewiesen.
 * @property messages Die Liste der Nachrichten innerhalb des Chats. Initialisiert als leere MutableList.
 */
data class Chat(
    @DocumentId
    val chatId: String = "",
    val messages: MutableList<Message> = mutableListOf()
)
