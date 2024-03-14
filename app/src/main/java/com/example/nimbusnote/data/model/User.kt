package com.example.nimbusnote.data.model

import com.google.firebase.firestore.DocumentId

data class User(
    var name: String = "",
    var handyNumber: String = "",
    var adress: String = "",
    var userName: String = "",
    var userImage: String = "",

    @DocumentId
    val userId: String = ""
)

