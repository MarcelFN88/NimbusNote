package com.example.nimbusnote.data.model

/**
 * Repräsentiert die allgemeinen Wetterbedingungen an einem bestimmten Ort.
 *
 * @property id Die eindeutige ID, die den Wetterzustand klassifiziert (z.B. 800 steht für klaren Himmel).
 * @property main Eine kurze Beschreibung des Hauptwetterzustands (z.B. "Clear").
 * @property description Eine detailliertere Beschreibung des Wetterzustands (z.B. "klarer Himmel").
 * @property icon Der Code des Icons, das den Wetterzustand visuell repräsentiert.
 */
data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)
