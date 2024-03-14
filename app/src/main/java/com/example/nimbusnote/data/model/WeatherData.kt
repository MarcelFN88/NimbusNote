package com.example.nimbusnote.data.model

data class WeatherData(
    val coord: Coord?,
    val weather: List<Weather>?,
    val main: Main?,
    val sys: Sys?,
    val id: Int?,
    val name: String?,
    val cod: Int?
)
