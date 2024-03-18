package com.example.nimbusnote.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Repräsentiert eine Notiz mit Textinhalt.
 *
 * @property text Der Textinhalt der Notiz. Standardmäßig leer.
 * @property id Die eindeutige ID der Notiz, automatisch von Firebase Firestore zugewiesen. Standardmäßig leer.
 */
data class Note(
    val text: String = "",
    val userId: String = "",
    @DocumentId
    var id: String = "",
    val noteNumber: Int = 0,
)
