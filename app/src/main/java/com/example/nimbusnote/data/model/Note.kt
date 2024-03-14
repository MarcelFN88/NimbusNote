package com.example.nimbusnote.data.model

import com.google.firebase.firestore.DocumentId

data class Note (
    val text: String = "",
    @DocumentId
    val id: String = ""

)