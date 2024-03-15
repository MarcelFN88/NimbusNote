package com.example.nimbusnote.viewModels

import android.net.Uri
import android.util.Log
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

/**
 * Verwaltet die Interaktion mit Firebase, einschließlich Authentifizierung, Nutzerdaten, Notizen und Nachrichten.
 * Stellt LiveData zur Verfügung, um die UI reaktiv auf Datenänderungen zu aktualisieren.
 */
class FirebaseViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val store = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: MutableLiveData<FirebaseUser?>
        get() = _currentUser

    private val _userProfileImageUrl = MutableLiveData<String>()
    val userProfileImageUrl: LiveData<String>
        get() = _userProfileImageUrl

    lateinit var userRef: DocumentReference
    lateinit var currentChat: DocumentReference

    val notesRef = store.collection("Notes")
    val usersRef = store.collection("Users")

    lateinit var currentUserId: String

    init {
        auth.currentUser?.let {
            userRef = store.collection("Users").document(it.uid)
            currentUserId = it.uid
            loadNotes()
        }
    }

    /**
     * Lädt das ausgewählte Bild in Firebase Storage hoch und aktualisiert das Profilbild des Nutzers.
     * @param uri Die URI des hochzuladenden Bildes.
     */
    fun uploadImage(uri: Uri) {
        val imagesRef = storageRef.child("users/${auth.currentUser?.uid}/profile.jpg")
        val uploadTask = imagesRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                setUserImage(downloadUri.toString())
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Hochladen des Bildes: ", exception)
        }
    }

    private fun setUserImage(uri: String) {
        userRef.update("userImage", uri).addOnSuccessListener {
            _userProfileImageUrl.postValue(uri)
        }.addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Aktualisieren des Profilbildes: ", exception)
        }
    }

    /**
     * Speichert eine neue Notiz in Firebase Firestore.
     * @param note Die zu speichernde Notiz.
     */
    fun saveNote(note: Note) {
        notesRef.add(note).addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Speichern der Notiz: ", exception)
        }
    }

    /**
     * Lädt alle Notizen aus Firebase Firestore und aktualisiert die LiveData-Liste `_notes`.
     */
    private fun loadNotes() {
        notesRef.addSnapshotListener { value, error ->
            error?.let {
                Log.e("FirebaseViewModel", "Fehler beim Laden der Notizen: ", it)
                return@addSnapshotListener
            }
            val myNotesList = value?.map { it.toObject(Note::class.java) } ?: listOf()
            _notes.value = myNotesList
        }
    }

    /**
     * Löscht eine spezifizierte Notiz aus Firebase Firestore.
     * @param note Die zu löschende Notiz.
     */
    fun deleteNote(note: Note) {
        notesRef.document(note.id).delete().addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Löschen der Notiz: ", exception)
        }
    }

    /**
     * Registriert einen neuen Nutzer mit E-Mail und Passwort in Firebase Auth und initialisiert den Nutzer in Firestore.
     * @param email Die E-Mail-Adresse des Nutzers.
     * @param password Das Passwort des Nutzers.
     * @param userName Der Benutzername des Nutzers.
     */
    fun register(email: String, password: String, userName: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                auth.currentUser?.let {
                    it.sendEmailVerification()
                    usersRef.document(it.uid).set(User(userName = userName))
                    currentUserId = it.uid
                    logout()
                }
            } else {
                Log.e("FirebaseViewModel", "Registrierungsfehler: ", authResult.exception!!)
            }
        }
    }

    /**
     * Meldet den Nutzer mit E-Mail und Passwort an und aktualisiert `currentUser` LiveData.
     * @param email Die E-Mail-Adresse des Nutzers.
     * @param password Das Passwort des Nutzers.
     */
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                _currentUser.value = auth.currentUser
            } else {
                Log.e("FirebaseViewModel", "Loginfehler: ", authResult.exception!!)
            }
        }
    }

    /**
     * Sendet eine E-Mail zur Passwortrücksetzung an die angegebene Adresse.
     * @param email Die E-Mail-Adresse, an die die Passwortrücksetzungsmail gesendet werden soll.
     */
    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Senden der Passwortrücksetzungsmail: ", exception)
        }
    }

    /**
     * Meldet den aktuellen Nutzer ab und setzt die `currentUser` LiveData zurück.
     */
    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }

    /**
     * Aktualisiert die Nutzerdaten im Firestore.
     * @param user Das User-Objekt mit den aktualisierten Daten.
     */
    fun updateUser(user: User) {
        userRef.set(user).addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Aktualisieren der Nutzerdaten: ", exception)
        }
    }

    /**
     * Initialisiert `currentChat` mit einem eindeutigen Chat-Dokument basierend auf den Nutzer-IDs.
     * @param chatPartnerId Die Nutzer-ID des Chatpartners.
     */
    fun setCurrentChat(chatPartnerId: String) {
        val chatId = chatIdGenerator(currentUserId, chatPartnerId)
        currentChat = store.collection("Chats").document(chatId)

        currentChat.get().addOnCompleteListener { task ->
            if (!task.isSuccessful || !task.result.exists()) {
                currentChat.set(Chat()).addOnFailureListener { exception ->
                    Log.e("FirebaseViewModel", "Fehler beim Initialisieren des Chats: ", exception)
                }
            }
        }
    }

    /**
     * Sendet eine neue Nachricht im aktuellen Chat.
     * @param text Der Text der Nachricht.
     */
    fun sendNewMessage(text: String) {
        currentChat.update("messages", FieldValue.arrayUnion(Message(text, currentUserId))).addOnFailureListener { exception ->
            Log.e("FirebaseViewModel", "Fehler beim Senden der Nachricht: ", exception)
        }
    }

    /**
     * Erzeugt eine eindeutige ID für einen Chat basierend auf den Nutzer-IDs.
     * @param id1 Die Nutzer-ID des einen Nutzers.
     * @param id2 Die Nutzer-ID des anderen Nutzers.
     * @return Eine eindeutige Chat-ID.
     */
    private fun chatIdGenerator(id1: String, id2: String): String {
        return listOf(id1, id2).sorted().joinToString()
    }
}
