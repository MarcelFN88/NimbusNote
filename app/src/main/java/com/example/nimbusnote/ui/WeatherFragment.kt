package com.example.nimbusnote.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.viewModel.MainViewModel
import com.example.nimbusnote.adapter.WeatherAdapter
import com.example.nimbusnote.data.model.WeatherData
import com.example.nimbusnote.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherAdapter = WeatherAdapter(listOf()) { weatherData ->
            showDeleteCityDialog(weatherData)
        }
        binding.weatherRecyclerView.adapter = weatherAdapter

        viewModel.weatherList.observe(viewLifecycleOwner) { weatherList ->
            weatherAdapter.updateWeatherList(weatherList)
        }

        binding.addCityButton.setOnClickListener {
            showAddCityDialog()
        }
    }

    private fun showAddCityDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Stadt hinzufügen")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Hinzufügen") { _, _ ->
            val cityName = input.text.toString()
            viewModel.fetchWeatherData(cityName)
        }

        builder.setNegativeButton("Abbrechen") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showDeleteCityDialog(weatherData: WeatherData) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Stadt löschen")
        builder.setMessage("Möchten Sie ${weatherData.name} wirklich löschen?")

        builder.setPositiveButton("Ja") { _, _ ->
            viewModel.deleteCity(weatherData.name!!)
        }

        builder.setNegativeButton("Nein") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

}
