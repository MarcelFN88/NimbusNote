package com.example.nimbusnote.data.remote

import com.example.nimbusnote.data.model.WeatherData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Stellt Dienste für den Zugriff auf die Wetter-API bereit.
 */
object ApiService {

    // Basis-URL der OpenWeatherMap-API.
    const val BASE_URL = "https://api.openweathermap.org/"

    // Ihr API-Schlüssel für den Zugriff auf die OpenWeatherMap-API.
    var apiKey = "192688db03dfb0e1627f2b59f6dd8cd4"

    // Basis-URL für den Zugriff auf Wetterbilder.
    const val IMAGE_BASE_URL = "http://openweathermap.org/img/wn/"

    // Suffix für hochauflösende Wetterbilder.
    const val IMAGE_SUFFIX = "@4x.png"

    // Konfiguration des Moshi JSON-Parsers.
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Erstellung und Konfiguration des Retrofit-Clients.
    private val retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

    /**
     * Definiert die Schnittstelle für den Zugriff auf Wetterdaten über die Wetter-API.
     */
    interface WeatherApiService {
        /**
         * Ruft die Wetterdaten für eine bestimmte Stadt ab.
         *
         * @param name Der Name der Stadt, für die das Wetter abgefragt werden soll.
         * @param apiKey Der API-Schlüssel für den Zugriff auf den Dienst.
         * @return Die Wetterdaten für die angegebene Stadt.
         */
        @GET("data/2.5/weather")
        suspend fun getWeatherData(
            @Query("q") name: String, @Query("appid") apiKey: String
        ): WeatherData
    }

    /**
     * Bereitstellung des Retrofit-Services zur Abfrage der Wetterdaten.
     */
    object WeatherApi {
        val retrofitService: WeatherApiService by lazy {
            retrofit.create(WeatherApiService::class.java)
        }
    }
}
