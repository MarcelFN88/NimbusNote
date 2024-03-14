package com.example.nimbusnote.viewModels

import android.net.Uri
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nimbusnote.data.model.Chat
import com.example.nimbusnote.data.model.Message
import com.example.nimbusnote.data.model.Note
import com.example.nimbusnote.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FirebaseViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val store = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val _notes = MutableLiveData<List<Note>>()
    val note: LiveData<List<Note>> = _notes
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: MutableLiveData<FirebaseUser?>
        get() = _currentUser

    lateinit var userRef: DocumentReference
    lateinit var currentChat: DocumentReference

    val notesRef = store.collection("Notes")
    val usersRef = store.collection("Users")

    lateinit var currentUserId: String

    init {
        if (auth.currentUser != null) {

            userRef = store.collection("Users").document(auth.currentUser!!.uid)

            currentUserId = auth.currentUser!!.uid
        }
        loadNotes()
    }


    fun uploadImage(uri: Uri) {
        val imagesRef = storageRef.child("image/${auth.currentUser!!.uid}/userImage")
        val uploadTask = imagesRef.putFile(uri)

        uploadTask.addOnCompleteListener() {
            imagesRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    setUserImage(it.result)
                }
            }
        }
    }

    private fun setUserImage(uri: Uri) {
        userRef.update("userImage", uri.toString())
    }

    fun saveNote(note: Note) {
        notesRef.add(note)
    }

    private fun loadNotes() {
        notesRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val myNotesList = value.map { it.toObject(Note::class.java) }
                _notes.value = myNotesList
            }
        }
    }

    fun deleteNote(note: Note) {
        notesRef.document(note.id).delete()
    }

    fun register(email: String, password: String, userName: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                auth.currentUser?.sendEmailVerification()
                userRef = store.collection("Users").document(auth.currentUser!!.uid)
                usersRef.document(auth.currentUser!!.uid).set(User(userName = userName))
                currentUserId = auth.currentUser!!.uid
                userRef.set(User())
                logout()
            } else {
                Log.e("Firebase", "${authResult.exception}")
            }
        }
    }

    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                if (auth.currentUser!!.isEmailVerified) {
                    userRef = store.collection("Users").document(auth.currentUser!!.uid)
                    _currentUser.value = auth.currentUser
                    currentUserId = auth.currentUser!!.uid
                } else {
                    Log.e("Firebase", "Email is not verified")
                }
            } else {
                Log.e("Firebase", "${authResult.exception}")
            }
        }
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = auth.currentUser
    }

    fun updateUser(user: User) {
        userRef.set(user)
    }

    fun setCurrentChat(chatPartnerId: String) {
        val chatId = chatIdGenerator(currentUserId, chatPartnerId)
        currentChat = store.collection("Chats").document(chatId)

        currentChat.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
                if (result != null) {
                    if (!result.exists()) {
                        currentChat.set(Chat())
                    }

                }
            }
        }
    }

    fun sendNewMessage(text: String) {
        currentChat.update("messages", FieldValue.arrayUnion(Message(text, currentUserId)))
    }

    private fun chatIdGenerator(id1: String, id2: String): String {
        var ids = listOf(id1, id2)
        ids = ids.sorted()
        return ids[0] + ids[1]
    }

}
