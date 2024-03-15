package com.example.nimbusnote.data.model

/**
 * Repräsentiert geografische Koordinaten.
 *
 * @property lon Die Längenkoordinate (Longitude) als Double. Kann null sein, wenn nicht definiert.
 * @property lat Die Breitenkoordinate (Latitude) als Double. Kann null sein, wenn nicht definiert.
 */
data class Coord(
    val lon: Double?,
    val lat: Double?
)
