package com.example.nimbusnote.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusnote.data.Repository
import com.example.nimbusnote.data.model.WeatherData
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repository = Repository()

    val weatherData = MutableLiveData<WeatherData?>()
    val errorMessage = MutableLiveData<String>()

    fun getWeather(cityName: String) {
        viewModelScope.launch {
            val data = repository.getWeatherData(cityName)
            if (data != null) {
                weatherData.postValue(data)
            } else {
                errorMessage.postValue("Fehler beim Abrufen der Wetterdaten.")
            }
        }
    }
}
