package com.example.nimbusnote.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusnote.data.Repository
import com.example.nimbusnote.data.model.WeatherData
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private val repository = Repository()

    // Ändere von einem einzelnen WeatherData Objekt zu einer Liste von WeatherData
    val weatherList = MutableLiveData<List<WeatherData>>()
    val errorMessage = MutableLiveData<String>()

    // Funktion, um Wetterdaten für mehrere Städte zu erhalten
    fun getWeatherForMultipleCities(cities: List<String>) {
        viewModelScope.launch {
            val weatherDataList = ArrayList<WeatherData>()
            cities.forEach { cityName ->
                val data = repository.getWeatherData(cityName)
                data?.let { weatherDataList.add(it) }
            }
            if (weatherDataList.isNotEmpty()) {
                weatherList.postValue(weatherDataList)
            } else {
                errorMessage.postValue("Fehler beim Abrufen der Wetterdaten.")
            }
        }
    }

    fun addCity(cityName: String) {
        viewModelScope.launch {
            val newWeatherData = repository.getWeatherData(cityName)
            newWeatherData?.let {
                val currentList = weatherList.value ?: listOf()
                weatherList.postValue(currentList + newWeatherData)
            }
        }
    }

}
