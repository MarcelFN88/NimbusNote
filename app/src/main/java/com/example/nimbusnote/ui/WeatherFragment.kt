package com.example.nimbusnote.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.adapter.UserAdapter
import com.example.nimbusnote.viewModel.MainViewModel
import com.example.nimbusnote.adapter.WeatherAdapter
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.data.model.WeatherData
import com.example.nimbusnote.databinding.FragmentWeatherBinding

/**
 * Fragment für die Anzeige und Verwaltung von Wetterdaten.
 */
class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val weatherAdapter by lazy { WeatherAdapter(mutableListOf()) { weatherData ->
        deleteCity(weatherData)
    } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.onAppStart()
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Entfernen des Observers, um nach der Zerstörung des Fragments keine Aktualisierungen mehr zu erhalten
        viewModel.weatherList.removeObservers(viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzen des Adapters für die Wetterdatenliste
        binding.weatherRecyclerView.adapter = weatherAdapter

        // Beobachten der Wetterdatenliste und Aktualisierung des Adapters bei Änderungen
        viewModel.weatherList.observe(viewLifecycleOwner) { weatherList ->
            weatherAdapter.setWeatherList(weatherList)
        }

        // Hinzufügen eines Click Listeners für den Button zum Hinzufügen einer Stadt
        binding.addCityButton.setOnClickListener {
            showAddCityDialog()
        }

        // Beobachten der Benutzerliste und Aktualisieren der Benutzerliste
        viewModel.users.observe(viewLifecycleOwner) { userList ->
            updateUsersList(userList)
        }

        // Laden der Benutzerliste
        viewModel.loadUsers()

        // Hinzufügen eines Snapshot Listeners für Änderungen an der Benutzerliste in der Datenbank
        viewModel.usersRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                // Konvertieren der Firebase-Daten in eine Liste von Benutzern
                val userList = value.map { it.toObject(User::class.java) }.toMutableList()
                // Entfernen des aktuellen Benutzers aus der Liste
                userList.removeAll { it.userId == viewModel.currentUserId }
                // Aktualisieren des Adapters für die Benutzerliste
                binding.chatUserRV.adapter = UserAdapter(userList, viewModel)
            }
        }
    }

    /**
     * Zeigt einen Dialog zum Hinzufügen einer Stadt an.
     */
    private fun showAddCityDialog() {
        val input = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("Stadt hinzufügen")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val cityName = input.text.toString()
                if (cityName.isNotBlank()) {
                    viewModel.saveCity(cityName)
                } else {
                    Toast.makeText(requireContext(), "Stadtnamen eingeben", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    /**
     * Aktualisiert die Benutzerliste im Adapter.
     */
    private fun updateUsersList(userList: List<User>) {
        val filteredUserList = userList.filter { it.userId != viewModel.currentUserId }
        binding.chatUserRV.adapter = UserAdapter(filteredUserList, viewModel)
    }

    /**
     * Zeigt einen Dialog zum Löschen einer Stadt an.
     */
    private fun deleteCity(city: WeatherData) {
        AlertDialog.Builder(requireContext())
            .setTitle("Stadt löschen")
            .setMessage("Möchten Sie ${city.name} wirklich löschen?")
            .setPositiveButton("Ja") { _, _ ->
                city.name?.let { viewModel.deleteCity(it) }
            }
            .setNegativeButton("Nein", null)
            .show()
    }
}
