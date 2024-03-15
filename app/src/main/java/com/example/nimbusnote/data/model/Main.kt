package com.example.nimbusnote.data.model

/**
 * Hält Wetterinformationen wie Temperatur und Feuchtigkeit.
 *
 * @property temp Die aktuelle Temperatur als Double. Kann null sein.
 * @property feels_like Die gefühlte Temperatur als Double. Kann null sein.
 * @property temp_min Die minimale aktuelle Temperatur als Double. Kann null sein.
 * @property temp_max Die maximale aktuelle Temperatur als Double. Kann null sein.
 * @property pressure Der aktuelle Luftdruck als Integer. Kann null sein.
 * @property humidity Die aktuelle Luftfeuchtigkeit als Integer. Kann null sein.
 */
data class Main(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val humidity: Int?
)
