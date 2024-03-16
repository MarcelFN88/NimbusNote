package com.example.nimbusnote.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbusnote.data.Repository
import com.example.nimbusnote.data.model.WeatherData
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repository = Repository()

    private val _weatherList = MutableLiveData<List<WeatherData>>()
    val weatherList: LiveData<List<WeatherData>>
        get() = _weatherList

    fun fetchWeatherData(cityName: String) {
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


    fun deleteCity(cityName: String) {
        val currentWeatherList = _weatherList.value.orEmpty().toMutableList()
        currentWeatherList.removeAll { it.name == cityName }
        _weatherList.value = currentWeatherList
    }
}
