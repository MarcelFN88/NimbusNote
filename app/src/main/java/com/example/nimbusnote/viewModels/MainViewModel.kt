package com.example.nimbusnote.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusnote.data.Repository
import com.example.nimbusnote.data.model.WeatherData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

/**
 * `MainViewModel` koordiniert die Operationen im Zusammenhang mit der Verwaltung der Wetterdaten
 * und der gespeicherten Städte des Benutzers. Es ermöglicht das Hinzufügen, Entfernen und Anzeigen
 * von Städten sowie das Abrufen und Anzeigen von Wetterdaten für diese Städte.
 */
class MainViewModel : ViewModel() {
    private val repository = Repository()

    // Enthält eine Liste der Städtenamen, die der Benutzer gespeichert hat.
    val citiesList = MutableLiveData<List<String>>(listOf())

    // Enthält die Wetterdaten für die gespeicherten Städte des Benutzers.
    val weatherList = MutableLiveData<List<WeatherData>>()

    // Wird verwendet, um Fehlermeldungen an die Benutzeroberfläche zu senden.
    val errorMessage = MutableLiveData<String>()

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val citiesCollection = FirebaseFirestore.getInstance()
        .collection("Users").document(userId ?: "")
        .collection("Cities")

    init {
        loadCitiesFromFirestore()
    }

    /**
     * Speichert den Namen einer Stadt in Firestore und aktualisiert die Liste der gespeicherten Städte.
     * @param cityName Der Name der Stadt, die gespeichert werden soll.
     */
    fun saveCity(cityName: String) {
        citiesCollection.document(cityName).set(mapOf("name" to cityName))
            .addOnSuccessListener {
                loadCitiesFromFirestore()
            }
            .addOnFailureListener { e ->
                errorMessage.value = "Fehler beim Speichern der Stadt: ${e.message}"
            }
    }

    /**
     * Entfernt eine Stadt aus der Firestore-Datenbank und aktualisiert die Liste der gespeicherten Städte.
     * @param cityName Der Name der Stadt, die entfernt werden soll.
     */
    fun removeCity(cityName: String) {
        citiesCollection.document(cityName).delete()
            .addOnSuccessListener {
                loadCitiesFromFirestore()
            }
            .addOnFailureListener { e ->
                errorMessage.value = "Fehler beim Entfernen der Stadt: ${e.message}"
            }
    }

    /**
     * Lädt die Liste der vom Benutzer gespeicherten Städte aus Firestore.
     */
    private fun loadCitiesFromFirestore() {
        citiesCollection.get().addOnSuccessListener { result ->
            val cityNames = result.mapNotNull { it.getString("name") }
            citiesList.value = cityNames
        }.addOnFailureListener { e ->
            errorMessage.value = "Fehler beim Laden der Städte: ${e.message}"
        }
    }

    /**
     * Lädt die Wetterdaten für die gespeicherten Städte des Benutzers. Diese Methode verwendet Coroutines,
     * um asynchron auf die Wetterdaten zuzugreifen und sie sicher im UI-Thread zu aktualisieren.
     * @param cities Eine Liste der Städtenamen, für die Wetterdaten geladen werden sollen.
     */
    fun loadWeatherForSavedCities(cities: List<String>) {
        viewModelScope.launch {
            try {
                val weatherDataList = ArrayList<WeatherData>()
                cities.forEach { cityName ->
                    val data = repository.getWeatherData(cityName)
                    data?.let { weatherDataList.add(it) }
                }
                weatherList.value = weatherDataList
            } catch (e: Exception) {
                errorMessage.value = "Fehler beim Laden der Wetterdaten: ${e.message}"
            }
        }
    }
}
