package com.example.nimbusnote.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nimbusnote.R
import com.example.nimbusnote.data.model.WeatherData
import com.example.nimbusnote.data.remote.ApiService

/**
 * Adapter für die Wetteransicht, verwaltet die Darstellung von Wetterdaten.
 *
 * @param weatherList Liste von Wetterdaten.
 * @param onCityLongPressed Callback-Funktion, die aufgerufen wird, wenn ein Langklick auf eine Stadt erfolgt.
 */
class WeatherAdapter(
    private var weatherList: List<WeatherData>,
    private val onCityLongPressed: (String) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    /**
     * ViewHolder für Wetterdaten, hält die Ansicht und die Bindungen für Stadtname, Temperatur und Wettericon.
     */
    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityNameTextView: TextView = view.findViewById(R.id.cityNameTextView)
        val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
        val weatherIconImageView: ImageView = view.findViewById(R.id.weatherIconImageView)
    }

    /**
     * Erstellt die ViewHolder für Wetterdaten.
     *
     * @param parent Der übergeordnete ViewGroup.
     * @param viewType Der Typ der Ansicht.
     * @return Eine Instanz von WeatherViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    /**
     * Bindet die Wetterdaten an die Ansicht. Setzt Stadtname, Temperatur und lädt das Wettericon.
     * Implementiert einen Langklick-Listener, um eine Stadt zu löschen.
     *
     * @param holder Der ViewHolder der Ansicht.
     * @param position Die Position der Wetterdaten in der Liste.
     */
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = weatherList[position]

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Stadt löschen")
                .setMessage("Möchten Sie ${item.name} wirklich löschen?")
                .setPositiveButton("Löschen") { _, _ ->
                    item.name?.let { it1 -> onCityLongPressed(it1) }
                }
                .setNegativeButton("Abbrechen", null)
                .show()
            true
        }

        holder.cityNameTextView.text = item.name
        holder.temperatureTextView.text = "${item.main?.temp?.minus(273.15)?.toInt()}°C"
        Glide.with(holder.itemView.context)
            .load("${ApiService.IMAGE_BASE_URL}/${item.weather?.firstOrNull()?.icon}${ApiService.IMAGE_SUFFIX}")
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .into(holder.weatherIconImageView)
    }

    /**
     * Gibt die Größe der Wetterdatenliste zurück.
     *
     * @return Die Größe der Wetterdatenliste.
     */
    override fun getItemCount() = weatherList.size

    /**
     * Aktualisiert die Liste der Wetterdaten und benachrichtigt den Adapter über die Änderung.
     *
     * @param newWeatherList Die neue Liste von Wetterdaten.
     */
    fun updateWeatherList(newWeatherList: List<WeatherData>) {
        this.weatherList = newWeatherList
        notifyDataSetChanged()
    }
}
