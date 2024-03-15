package com.example.nimbusnote.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusnote.data.Repository
import com.example.nimbusnote.data.model.WeatherData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private val repository = Repository()
    val citiesList = MutableLiveData<List<String>>(listOf())
    val weatherList = MutableLiveData<List<WeatherData>>()
    val errorMessage = MutableLiveData<String>()

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val citiesCollection = FirebaseFirestore.getInstance()
        .collection("Users").document(userId ?: "")
        .collection("Cities")

    init {
        loadCitiesFromFirestore()
    }

    fun saveCity(cityName: String) {
        citiesCollection.document(cityName).set(mapOf("name" to cityName))
            .addOnSuccessListener {
                loadCitiesFromFirestore()
            }
            .addOnFailureListener { e ->
                errorMessage.value = "Fehler beim Speichern der Stadt: ${e.message}"
            }
    }

    fun removeCity(cityName: String) {
        citiesCollection.document(cityName).delete()
            .addOnSuccessListener {
                loadCitiesFromFirestore()
            }
            .addOnFailureListener { e ->
                errorMessage.value = "Fehler beim Entfernen der Stadt: ${e.message}"
            }
    }

    private fun loadCitiesFromFirestore() {
        citiesCollection.get().addOnSuccessListener { result ->
            val cityNames = result.mapNotNull { it.getString("name") }
            citiesList.value = cityNames
            loadWeatherForSavedCities(cityNames)
        }.addOnFailureListener { e ->
            errorMessage.value = "Fehler beim Laden der Städte: ${e.message}"
        }
    }

    fun loadWeatherForSavedCities(cities: List<String>) {
        viewModelScope.launch {
            val weatherDataList = ArrayList<WeatherData>()
            cities.forEach { cityName ->
                val data = repository.getWeatherData(cityName)
                data?.let { weatherDataList.add(it) }
            }
            weatherList.postValue(weatherDataList)
        }
    }

}
