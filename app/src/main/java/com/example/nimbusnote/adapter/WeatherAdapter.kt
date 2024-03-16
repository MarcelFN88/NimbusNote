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

class WeatherAdapter(
    private var weatherList: List<WeatherData>,
    private val onDeleteClickListener: (WeatherData) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityNameTextView: TextView = view.findViewById(R.id.cityNameTextView)
        val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
        val weatherIconImageView: ImageView = view.findViewById(R.id.weatherIconImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val weatherData = weatherList[position]
                    onDeleteClickListener(weatherData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

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
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    holder.weatherIconImageView.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Place holder cleaning code if needed
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // Error handling code here
                    Log.e("Glide", "Error loading image")
                    holder.weatherIconImageView.setImageDrawable(errorDrawable)
                }
            })

    }

    override fun getItemCount() = weatherList.size

    fun updateWeatherList(newWeatherList: List<WeatherData>) {
        this.weatherList = newWeatherList
        notifyDataSetChanged()
    }

}
