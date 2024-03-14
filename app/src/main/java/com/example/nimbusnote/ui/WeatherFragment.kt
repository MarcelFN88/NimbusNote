package com.example.nimbusnote.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nimbusnote.adapter.UserAdapter
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.databinding.FragmentWeatherBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel
import com.example.nimbusnote.viewModels.MainViewModel


/**
 * Fragment zur Anzeige des Wetters und einer Benutzerliste.
 */
class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()



    /**
     * Inflatiert das Layout für dieses Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Initialisiert das Fragment, nachdem die Ansicht erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Beobachtet die Benutzerliste aus dem ViewModel und aktualisiert den RecyclerView bei Änderungen.
        viewModel.users.observe(viewLifecycleOwner) { userList ->
            binding.chatUserRV.adapter = UserAdapter(userList, viewModel)

        }

        // Fügt einen SnapshotListener hinzu, um Änderungen in der Benutzerliste in Echtzeit zu erfassen.
        viewModel.usersRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val userList = value.map { it.toObject(User::class.java) }.toMutableList()
                // Entfernt den aktuellen Benutzer aus der Liste, um Selbstgespräche zu vermeiden.
                userList.removeAll { it.userId == viewModel.currentUserId }
                binding.chatUserRV.adapter = UserAdapter(userList, viewModel)
            }
        }
    }


}


