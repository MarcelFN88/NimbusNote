package com.example.nimbusnote.data.model

/**
 * Modelliert eine Nachricht in einem Chat.
 *
 * @property text Der Textinhalt der Nachricht. Standardmäßig leer.
 * @property sender Die Identität des Absenders der Nachricht. Standardmäßig leer.
 */
data class Message(
    val text: String = "",
    val sender: String = ""
)
