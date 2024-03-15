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
     * Ruft Wetterdaten für eine bestimmte Stadt ab.
     *
     * Die Methode verwendet eine Coroutine, um den Netzwerkaufruf auf einem Hintergrundthread durchzuführen.
     * Im Fehlerfall wird `null` zurückgegeben.
     *
     * @param cityName Der Name der Stadt, für die das Wetter abgefragt werden soll.
     * @return Die Wetterdaten für die angegebene Stadt oder `null` bei einem Fehler.
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
