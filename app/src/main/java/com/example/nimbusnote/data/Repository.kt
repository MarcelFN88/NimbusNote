package com.example.nimbusnote.data

import com.example.nimbusnote.data.model.WeatherData
import com.example.nimbusnote.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository-Klasse zur Abfrage von Wetterdaten.
 */
class Repository {

    /**
     * Ruft die Wetterdaten für eine bestimmte Stadt ab.
     *
     * @param cityName Der Name der Stadt, für die das Wetter abgefragt werden soll.
     * @return Die Wetterdaten für die angegebene Stadt.
     */
    suspend fun getWeatherData(cityName: String): WeatherData? {
        return withContext(Dispatchers.IO) {
            try {
                ApiService.WeatherApi.retrofitService.getWeatherData(cityName, ApiService.apiKey)
            } catch (e: Exception) {
                null
            }
        }
    }
}
