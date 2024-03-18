package com.example.nimbusnote.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusnote.data.Repository
import com.example.nimbusnote.data.model.Chat
import com.example.nimbusnote.data.model.Message
import com.example.nimbusnote.data.model.Note
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.data.model.WeatherData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

/**
 * ViewModel für die Hauptfunktionen der App.
 */
class MainViewModel : ViewModel() {

    private val repository = Repository()
    private val auth = FirebaseAuth.getInstance()
    private val store = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    val notesRef = store.collection("Notes")
    val usersRef = store.collection("Users")

    private val _notes = MutableLiveData<List<Note>>()
    val note: LiveData<List<Note>> = _notes
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    lateinit var currentUserId: String
    lateinit var currentChat: DocumentReference
    lateinit var userRef: DocumentReference
    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: MutableLiveData<FirebaseUser?>
        get() = _currentUser
    private val _weatherList = MutableLiveData<MutableList<WeatherData>>()
    val weatherList: LiveData<MutableList<WeatherData>>
        get() = _weatherList

    private var selectedImageUri: Uri? = null

    init {
        _currentUser.observeForever { user ->
            if (user != null) {
                initializeUserRef(user.uid)
                loadNotes()
                loadCities()
                currentUserId = auth.currentUser!!.uid
            }
        }
    }

    /**
     * Initialisiert den Benutzer-Verweis.
     * @param userId Die ID des aktuellen Benutzers.
     */
    private fun initializeUserRef(userId: String) {
        userRef = store.collection("Users").document(userId)
    }

    private val fetchedCities = mutableListOf<String>()
    /**
     * Holt Wetterdaten für den angegebenen Städtenamen.
     * @param cityName Der Name der Stadt, für die Wetterdaten abgerufen werden sollen.
     */
    private fun fetchWeatherData(cityName: String) {
        if (cityName !in fetchedCities) {
            viewModelScope.launch {
                try {
                    val weatherData = repository.getWeatherData(cityName)
                    weatherData?.let {
                        val updatedList = (_weatherList.value ?: mutableListOf()).toMutableList()
                        updatedList.add(weatherData)
                        _weatherList.value = updatedList
                        fetchedCities.add(cityName)
                    }
                } catch (_: Exception) {
                    // Fehlerbehandlung
                }
            }
        }
    }

    /**
     * Lädt ein Bild in Firebase Storage hoch.
     * @param uri Die URI des hochzuladenden Bildes.
     */
    fun uploadImage(uri: Uri) {
        val imagesRef = storageRef.child("image/${auth.currentUser!!.uid}/userImage")
        val uploadTask = imagesRef.putFile(uri)

        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                imagesRef.downloadUrl.addOnCompleteListener { uriTask ->
                    if (uriTask.isSuccessful) {
                        selectedImageUri = uriTask.result
                        setUserImage(selectedImageUri)
                    } else {
                        Log.e("Firebase", "Fehler beim Abrufen der Download-URL: ${uriTask.exception}")
                    }
                }
            } else {
                Log.e("Firebase", "Fehler beim Hochladen des Bildes: ${task.exception}")
            }
        }
    }

    /**
     * Setzt die URI des Benutzerbildes in der Datenbank.
     * @param uri Die URI des Benutzerbildes.
     */
    private fun setUserImage(uri: Uri?) {
        uri?.let {
            userRef.update("userImage", it.toString())
                .addOnSuccessListener {
                    Log.d("Firebase", "Bild-URI erfolgreich aktualisiert")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Fehler beim Aktualisieren der Bild-URI: $e")
                }
        }
    }

    /**
     * Ruft die URI des Benutzerbildes ab.
     * @return Die URI des Benutzerbildes.
     */
    fun getImageUri(): Uri? {
        return selectedImageUri
    }

    /**
     * Speichert eine Notiz in Firestore.
     * @param note Die zu speichernde Notiz.
     */
    fun saveNote(note: Note) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            // Die UserID der Notiz auf die des aktuellen Benutzers setzen
            notesRef.whereEqualTo("userId", user.uid).get().addOnSuccessListener { querySnapshot ->
                val noteNumber = querySnapshot.size() + 1 // Die Notiznummer ist die Anzahl der vorhandenen Notizen plus eins
                val newNote = note.copy(userId = user.uid, noteNumber = noteNumber)
                // Notiz in Firestore speichern
                notesRef.add(newNote)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firebase", "Notiz erfolgreich gespeichert mit ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Fehler beim Speichern der Notiz: $e")
                    }
            }
        }
    }

    /**
     * Lädt Notizen aus Firestore.
     */
    private fun loadNotes() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            // Nur die Notizen des aktuellen Benutzers laden und nach Notiznummer sortieren
            notesRef.whereEqualTo("userId", user.uid).orderBy("noteNumber")
                .addSnapshotListener { value, error ->
                    if (error == null && value != null) {
                        val myNotesList = value.map { it.toObject(Note::class.java) }
                        _notes.value = myNotesList
                    }
                }
        }
    }

    /**
     * Löscht eine Notiz aus Firestore.
     * @param note Die zu löschende Notiz.
     */
    fun deleteNote(note: Note) {
        notesRef.document(note.id).delete()
    }

    /**
     * Registriert einen neuen Benutzer.
     * @param email Die E-Mail des Benutzers.
     * @param password Das Passwort des Benutzers.
     * @param userName Der Benutzername des Benutzers.
     */
    fun register(email: String, password: String, userName: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                auth.currentUser?.sendEmailVerification()
                val newUser = User(userName = userName)
                userRef = store.collection("Users").document(auth.currentUser!!.uid)
                userRef.set(newUser)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Benutzer erfolgreich registriert")
                        currentUserId = auth.currentUser!!.uid
                        loadNotes()
                        logout()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Fehler beim Registrieren des Benutzers: $e")
                    }
            } else {
                Log.e("Firebase", "${authResult.exception}")
            }
        }
    }

    /**
     * Lädt Städte für den aktuellen Benutzer.
     */
    fun loadCities() {
        viewModelScope.launch {
            try {
                val userDocRef = store.collection("Users").document(currentUserId)
                userDocRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val cities = document.get("cities") as? List<String>
                            cities?.forEach { cityName ->
                                fetchWeatherData(cityName)
                            }
                            fetchedCities.clear()
                            fetchedCities.addAll(cities ?: emptyList())
                        } else {
                            Log.e("MainViewModel", "Dokument existiert nicht")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainViewModel", "Fehler beim Laden der Städte: $e")
                    }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Fehler beim Laden der Städte: $e")
            }
        }
    }

    /**
     * Meldet einen Benutzer an.
     * @param email Die E-Mail des Benutzers.
     * @param password Das Passwort des Benutzers.
     */
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                if (auth.currentUser!!.isEmailVerified) {
                    userRef = store.collection("Users").document(auth.currentUser!!.uid)
                    _currentUser.value = auth.currentUser
                    currentUserId = auth.currentUser!!.uid
                    loadNotes()
                    loadCities()
                } else {
                    Log.e("Firebase", "E-Mail ist nicht verifiziert")
                }
            } else {
                Log.e("Firebase", "${authResult.exception}")
            }
        }
    }

    /**
     * Sendet eine E-Mail zur Zurücksetzung des Passworts.
     * @param email Die E-Mail des Benutzers.
     */
    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    /**
     * Meldet den aktuellen Benutzer ab.
     */
    fun logout() {
        auth.signOut()
        _currentUser.value = auth.currentUser
    }

    /**
     * Aktualisiert die Benutzerinformationen.
     * @param user Das Benutzerobjekt mit den aktualisierten Informationen.
     */
    fun updateUser(user: User) {
        userRef.set(user)
    }

    /**
     * Lädt alle Benutzer.
     */
    fun loadUsers() {
        usersRef.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                val userList = snapshot.toObjects(User::class.java)
                _users.value = userList
            }
        }
    }

    /**
     * Legt den aktuellen Chat fest.
     * @param chatPartnerId Die ID des Chat-Partners.
     */
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

    /**
     * Sendet eine neue Nachricht im aktuellen Chat.
     * @param text Der Text der Nachricht.
     */
    fun sendNewMessage(text: String) {
        currentChat.update("messages", FieldValue.arrayUnion(Message(text, currentUserId)))
    }

    /**
     * Generiert eine Chat-ID.
     * @param id1 Die ID des ersten Teilnehmers.
     * @param id2 Die ID des zweiten Teilnehmers.
     * @return Die generierte Chat-ID.
     */
    private fun chatIdGenerator(id1: String, id2: String): String {
        var ids = listOf(id1, id2)
        ids = ids.sorted()
        return ids[0] + ids[1]
    }

    /**
     * Speichert eine Stadt für den aktuellen Benutzer.
     * @param cityName Der Name der zu speichernden Stadt.
     */
    fun saveCity(cityName: String) {
        viewModelScope.launch {
            try {
                val userDocRef = store.collection("Users").document(currentUserId)
                userDocRef.update("cities", FieldValue.arrayUnion(cityName))
                    .addOnSuccessListener {
                        Log.d("MainViewModel", "Stadt erfolgreich gespeichert")
                        fetchWeatherData(cityName)
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainViewModel", "Fehler beim Speichern der Stadt: $e")
                    }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Fehler beim Speichern der Stadt: $e")
            }
        }
    }

    /**
     * Führt notwendige Aktionen beim Start der App aus.
     */
    fun onAppStart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            initializeUserRef(user.uid)
            loadCities()
            currentUserId = auth.currentUser!!.uid
        }
    }

    /**
     * Löscht eine Stadt aus den gespeicherten Städten des aktuellen Benutzers.
     * @param cityName Der Name der zu löschenden Stadt.
     */
    fun deleteCity(cityName: String) {
        viewModelScope.launch {
            try {
                val userDocRef = store.collection("Users").document(currentUserId)
                userDocRef.update("cities", FieldValue.arrayRemove(cityName))
                    .addOnSuccessListener {
                        Log.d("MainViewModel", "Stadt erfolgreich gelöscht")
                        val updatedWeatherList = _weatherList.value?.filter { it.name != cityName }
                        _weatherList.value = updatedWeatherList?.toMutableList()
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainViewModel", "Fehler beim Löschen der Stadt: $e")
                    }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Fehler beim Löschen der Stadt: $e")
            }
        }
    }
}
