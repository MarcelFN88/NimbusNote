package com.example.nimbusnote.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Modelliert ein Benutzerprofil mit grundlegenden Informationen und Profilbild.
 *
 * @property name Der vollständige Name des Benutzers. Standardmäßig leer.
 * @property handyNumber Die Telefonnummer des Benutzers. Standardmäßig leer.
 * @property adress Die Adresse des Benutzers. Standardmäßig leer.
 * @property userName Der Benutzername des Benutzers. Standardmäßig leer.
 * @property userImage Die URL zum Profilbild des Benutzers. Standardmäßig leer.
 * @property userId Die eindeutige ID des Benutzers, automatisch von Firebase Firestore zugewiesen.
 */
data class User(
    var name: String = "",
    var handyNumber: String = "",
    var adress: String = "",
    var userName: String = "",
    var userImage: String = "",

    @DocumentId
    val userId: String = ""
)
