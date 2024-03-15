package com.example.nimbusnote.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.adapter.UserAdapter
import com.example.nimbusnote.adapter.WeatherAdapter
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.databinding.FragmentWeatherBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel
import com.example.nimbusnote.viewModels.MainViewModel

/**
 * Das `WeatherFragment` ist verantwortlich für die Darstellung von Wetterinformationen und einer Liste von Benutzern.
 * Es ermöglicht dem Benutzer, Städte zur Wetterbeobachtung hinzuzufügen oder zu entfernen und zeigt eine aktuelle Liste
 * von Benutzern an, die für Chat-Funktionen verfügbar sind. Das Fragment nutzt zwei ViewModel-Instanzen: `FirebaseViewModel`
 * für Benutzerdaten und `MainViewModel` für Wetterdaten. Die Darstellung erfolgt durch zwei separate RecyclerViews, einen für
 * das Wetter und einen für die Benutzerliste. Dialoge ermöglichen das einfache Hinzufügen und Entfernen von Städten.
 */
class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.users.observe(viewLifecycleOwner) { userList ->
            binding.chatUserRV.adapter = UserAdapter(userList, viewModel)
        }

        viewModel.usersRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val userList = value.map { it.toObject(User::class.java) }.toMutableList()
                userList.removeAll { it.userId == viewModel.currentUserId }
                binding.chatUserRV.adapter = UserAdapter(userList, viewModel)
            }
        }

       val weatherAdapter = WeatherAdapter(listOf()) { cityName ->
          showRemoveCityDialog(cityName)
       }
        binding.weatherRecyclerView.adapter = weatherAdapter

        mainViewModel.citiesList.observe(viewLifecycleOwner) { cities ->
            mainViewModel.loadWeatherForSavedCities(cities)
        }

        mainViewModel.weatherList.observe(viewLifecycleOwner) { weatherList ->
            weatherAdapter.updateWeatherList(weatherList)
        }

        binding.addCityButton.setOnClickListener {
            showAddCityDialog()
        }
    }

    /**
     * Zeigt einen Dialog zum Entfernen einer Stadt aus der Liste der beobachteten Städte.
     * @param cityName Name der Stadt, die entfernt werden soll.
     */
    private fun showRemoveCityDialog(cityName: String) {
                mainViewModel.removeCity(cityName)

    }

    /**
     * Zeigt einen Dialog zum Hinzufügen einer neuen Stadt zur Liste der beobachteten Städte.
     */
    private fun showAddCityDialog() {
        val editText = EditText(context).apply {
            hint = "Stadtname"
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Stadt hinzufügen")
            .setView(editText)
            .setPositiveButton("Hinzufügen") { _, _ ->
                val cityName = editText.text.toString()
                if (cityName.isNotBlank()) {
                    mainViewModel.saveCity(cityName)
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }
}
