package com.example.nimbusnote.data.remote

import com.example.nimbusnote.data.model.WeatherData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiService {

    const val BASE_URL = "https://api.openweathermap.org/"

    var apiKey = "192688db03dfb0e1627f2b59f6dd8cd4"


    const val IMAGE_BASE_URL = "http://openweathermap.org/img/wn/"
    const val IMAGE_SUFFIX = "@4x.png"


    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

    interface WeatherApiService {
        @GET("data/2.5/weather")
        suspend fun getWeatherData(
            @Query("q") name: String, @Query("appid") apiKey: String
        ): WeatherData
    }

    object WeatherApi {
        val retrofitService: WeatherApiService by lazy {
            retrofit.create(WeatherApiService::class.java)
        }
    }
}
