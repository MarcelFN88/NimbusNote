package com.example.nimbusnote.data.model

/**
 * Repräsentiert systembezogene Wetterinformationen, wie sie von der Wetter-API bereitgestellt werden.
 *
 * @property country Der Ländercode (z.B. "DE" für Deutschland), der das Land angibt, zu dem die Wetterdaten gehören. Kann null sein.
 * @property sunrise Der Zeitpunkt des Sonnenaufgangs als Unix-Zeitstempel in Sekunden. Kann null sein.
 * @property sunset Der Zeitpunkt des Sonnenuntergangs als Unix-Zeitstempel in Sekunden. Kann null sein.
 */
data class Sys(
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)
