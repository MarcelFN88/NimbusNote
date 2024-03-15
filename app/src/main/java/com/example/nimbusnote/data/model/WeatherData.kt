package com.example.nimbusnote.data.model

/**
 * Enthält detaillierte Wetterdaten für einen spezifischen Ort.
 *
 * @property coord Die geografischen Koordinaten des Ortes, für den die Wetterdaten gelten.
 * @property weather Eine Liste von `Weather`-Objekten, die die allgemeinen Wetterbedingungen beschreiben.
 * @property main Ein `Main`-Objekt, das Kerninformationen wie Temperatur und Luftfeuchtigkeit hält.
 * @property sys Ein `Sys`-Objekt mit systembezogenen Informationen, z.B. zum Land und zu Sonnenauf- und -untergangszeiten.
 * @property id Die eindeutige ID der Wetterstation oder des Ortes, von dem die Daten stammen.
 * @property name Der Name des Ortes, für den die Wetterdaten gelten.
 * @property cod Der HTTP-Statuscode der API-Antwort, der den Erfolg der Anfrage anzeigt.
 */
data class WeatherData(
    val coord: Coord?,
    val weather: List<Weather>?,
    val main: Main?,
    val sys: Sys?,
    val id: Int?,
    val name: String?,
    val cod: Int?
)
