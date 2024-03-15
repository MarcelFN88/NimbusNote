package com.example.nimbusnote.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nimbusnote.data.model.WeatherData
import com.example.nimbusnote.data.remote.ApiService
import com.example.nimbusnote.R
import com.example.nimbusnote.viewModels.MainViewModel


class WeatherAdapter(private var weatherList: List<WeatherData>,private val onCityLongPressed: (String) -> Unit) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityNameTextView: TextView = view.findViewById(R.id.cityNameTextView)
        val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
        val weatherIconImageView: ImageView = view.findViewById(R.id.weatherIconImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = weatherList[position]

        holder.itemView.setOnLongClickListener {
            // Zeige Bestätigungsdialog
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Stadt löschen")
                .setMessage("Möchten Sie ${item.name} wirklich löschen?")
                .setPositiveButton("Löschen") { dialog, which ->
                    item.name?.let { it1 -> onCityLongPressed(it1) } // Rufe den Callback auf
                }
                .setNegativeButton("Abbrechen", null)
                .show()
            true
        }


        holder.cityNameTextView.text = item.name
        holder.temperatureTextView.text =
            "${item.main?.temp?.minus(273.15)?.toInt()}°C" // Umrechnung von Kelvin in Celsius
        Glide.with(holder.itemView.context)
            .load("${ApiService.IMAGE_BASE_URL}/${item.weather?.firstOrNull()?.icon}${ApiService.IMAGE_SUFFIX}")
            .placeholder(R.drawable.ic_placeholder) // Ein Platzhalter-Bild
            .error(R.drawable.ic_error) // Ein Fehler-Bild, wenn das Laden scheitert
            .into(holder.weatherIconImageView)
    }

    override fun getItemCount() = weatherList.size

    fun updateWeatherList(newWeatherList: List<WeatherData>) {
        this.weatherList = newWeatherList
        notifyDataSetChanged()
    }
}
