package com.example.nimbusnote.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.nimbusnote.R
import com.example.nimbusnote.data.model.WeatherData
import com.example.nimbusnote.data.remote.ApiService

/**
 * Adapter zur Anzeige von Wetterdaten in einem RecyclerView.
 *
 * @property weatherList Liste der Wetterdaten.
 * @property onItemClickListener Lambda-Ausdruck, der aufgerufen wird, wenn ein Wetterelement angeklickt wird.
 */
class WeatherAdapter(
    private var weatherList: MutableList<WeatherData>,
    private val onItemClickListener: (WeatherData) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    /**
     * ViewHolder für Wetterdaten.
     *
     * @param view Die View, die den ViewHolder darstellt.
     */
    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityNameTextView: TextView = view.findViewById(R.id.cityNameTextView)
        val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
        val weatherIconImageView: ImageView = view.findViewById(R.id.weatherIconImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = weatherList[position]
                    onItemClickListener.invoke(item)
                }
            }
        }
    }

    /**
     * Erstellt einen neuen ViewHolder, wenn dies erforderlich ist.
     *
     * @param parent Die ViewGroup, in die die neue Ansicht eingefügt wird.
     * @param viewType Der Ansichtstyp der neuen Ansicht.
     * @return WeatherViewHolder mit dem aufgeblasenen Layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    /**
     * Bindet die Daten an den ViewHolder an der angegebenen Position.
     *
     * @param holder Der ViewHolder, an den die Daten gebunden werden sollen.
     * @param position Die Position des Elements im Datensatz.
     */
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = weatherList[position]

        holder.cityNameTextView.text = item.name
        holder.temperatureTextView.text = "${item.main?.temp?.minus(273.15)?.toInt()}°C"
        Log.d("ImageUrl", "${ApiService.IMAGE_BASE_URL}/${item.weather?.firstOrNull()?.icon}${ApiService.IMAGE_SUFFIX}")
        Log.d("IconValue", "Icon Value: ${item.weather?.firstOrNull()?.icon}")

        Glide.with(holder.itemView.context)
            .load("${ApiService.IMAGE_BASE_URL}/${item.weather?.firstOrNull()?.icon}${ApiService.IMAGE_SUFFIX}")
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .into(holder.weatherIconImageView)
    }

    /**
     * Gibt die Gesamtzahl der Elemente im Datensatz zurück.
     *
     * @return Int, der die Gesamtzahl der Elemente darstellt.
     */
    override fun getItemCount() = weatherList.size

    /**
     * Aktualisiert die Wetterliste mit einer neuen Liste.
     *
     * @param newWeatherList Die neue Liste von Wetterdaten.
     */
    fun updateWeatherList(newWeatherList: List<WeatherData>) {
        this.weatherList = newWeatherList.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Setzt die Wetterliste auf eine neue Liste.
     *
     * @param newWeatherList Die neue Liste von Wetterdaten.
     */
    fun setWeatherList(newWeatherList: List<WeatherData>) {
        weatherList.clear() // Vorhandene Daten löschen
        weatherList.addAll(newWeatherList) // Neue Daten hinzufügen
        notifyDataSetChanged()
    }
}
