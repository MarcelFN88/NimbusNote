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


class MainViewModel: ViewModel() {

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


    private fun initializeUserRef(userId: String) {
        userRef = store.collection("Users").document(userId)
    }

    private fun fetchWeatherData(cityName: String) {
        viewModelScope.launch {
            try {
                val weatherData = repository.getWeatherData(cityName)
                weatherData?.let {
                    val updatedList = (_weatherList.value ?: emptyList()).toMutableList()
                    updatedList.add(weatherData)
                    _weatherList.value = updatedList
                }
            } catch (_: Exception) {

            }
        }
    }

    fun uploadImage(uri: Uri) {
        val imagesRef = storageRef.child("image/${auth.currentUser!!.uid}/userImage")
        val uploadTask = imagesRef.putFile(uri)

        uploadTask.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Bild erfolgreich hochgeladen, jetzt Uri aktualisieren
                imagesRef.downloadUrl.addOnCompleteListener { uriTask ->
                    if (uriTask.isSuccessful) {
                        selectedImageUri = uriTask.result
                        setUserImage(selectedImageUri) // Update Uri in der Datenbank
                    } else {
                        Log.e("Firebase", "Error getting download URL: ${uriTask.exception}")
                    }
                }
            } else {
                Log.e("Firebase", "Error uploading image: ${task.exception}")
            }
        }
    }

    // Aktualisiert das Uri in der Datenbank
    private fun setUserImage(uri: Uri?) {
        uri?.let {
            userRef.update("userImage", it.toString())
                .addOnSuccessListener {
                    Log.d("Firebase", "Image Uri updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Error updating image Uri: $e")
                }
        }
    }


    fun getImageUri(): Uri? {
        return selectedImageUri
    }

    fun saveNote(note: Note) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val newNote = note.copy(userId = user.uid) // Setzen Sie den userId des aktuellen Benutzers
            val noteDocRef = notesRef.document(user.uid) // Verwenden Sie die Benutzer-ID als Dokument-ID für die Notiz
            noteDocRef.set(newNote)
                .addOnSuccessListener {
                    Log.d("Firebase", "Note saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Error saving note: $e")
                }
        }
    }



    private fun loadNotes() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            notesRef.whereEqualTo("userId", user.uid).addSnapshotListener { value, error ->
                if (error == null && value != null) {
                    val myNotesList = value.map { it.toObject(Note::class.java) }
                    _notes.value = myNotesList
                }
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
                // Hier speichern Sie den Benutzernamen in Firebase
                val newUser = User(userName = userName)
                userRef = store.collection("Users").document(auth.currentUser!!.uid)
                userRef.set(newUser)
                    .addOnSuccessListener {
                        Log.d("Firebase", "User registered successfully")
                        currentUserId = auth.currentUser!!.uid
                        loadNotes() // Notizen laden nach erfolgreicher Registrierung
                        logout()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error registering user: $e")
                    }
            } else {
                Log.e("Firebase", "${authResult.exception}")
            }
        }
    }


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
                        } else {
                            Log.e("MainViewModel", "No such document")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainViewModel", "Error loading cities: $e")
                    }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading cities: $e")
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
                    loadNotes()
                    loadCities()
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
    fun loadUsers() {
        usersRef.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                val userList = snapshot.toObjects(User::class.java)
                _users.value = userList
            }
        }
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

    fun saveCity(cityName: String) {
        viewModelScope.launch {
            try {
                // Speichern der Stadt unter der Benutzer-ID in der Firebase-Datenbank
                val userDocRef = store.collection("Users").document(currentUserId)
                userDocRef.update("cities", FieldValue.arrayUnion(cityName))
                    .addOnSuccessListener {
                        Log.d("MainViewModel", "City saved successfully")
                        fetchWeatherData(cityName) // Daten für die neu hinzugefügte Stadt abrufen
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainViewModel", "Error saving city: $e")
                    }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error saving city: $e")
            }
        }
    }
    fun onAppStart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            initializeUserRef(user.uid)
            loadCities()
            currentUserId = auth.currentUser!!.uid
        }
    }
    fun deleteCity(cityName: String) {
        viewModelScope.launch {
            try {
                val userDocRef = store.collection("Users").document(currentUserId)
                userDocRef.update("cities", FieldValue.arrayRemove(cityName))
                    .addOnSuccessListener {
                        Log.d("MainViewModel", "City deleted successfully")
                        // Aktualisiere die Liste der Wetterdaten, um die gelöschte Stadt zu entfernen
                        val updatedWeatherList = _weatherList.value?.filter { it.name != cityName }
                        _weatherList.value = updatedWeatherList?.toMutableList()
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainViewModel", "Error deleting city: $e")
                    }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error deleting city: $e")
            }
        }
    }





}
