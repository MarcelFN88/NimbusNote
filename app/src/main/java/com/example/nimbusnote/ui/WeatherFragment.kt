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
        // Entfernen Sie den Observer, um zu verhindern, dass er nach der Zerstörung des Fragments aktualisiert wird
        viewModel.weatherList.removeObservers(viewLifecycleOwner)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weatherRecyclerView.adapter = weatherAdapter



        viewModel.weatherList.observe(viewLifecycleOwner) { weatherList ->
            weatherAdapter.setWeatherList(weatherList)
        }



        binding.addCityButton.setOnClickListener {
            showAddCityDialog()
        }

        viewModel.users.observe(viewLifecycleOwner) { userList ->
            updateUsersList(userList)
        }

        viewModel.loadUsers()


        viewModel.usersRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val userList = value.map { it.toObject(User::class.java) }.toMutableList()
                userList.removeAll { it.userId == viewModel.currentUserId }
                binding.chatUserRV.adapter = UserAdapter(userList, viewModel)
            }
        }
    }
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
    private fun updateUsersList(userList: List<User>) {

        val filteredUserList = userList.filter { it.userId != viewModel.currentUserId }
        binding.chatUserRV.adapter = UserAdapter(filteredUserList, viewModel)
    }
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
