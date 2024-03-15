package com.example.nimbusnote.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.nimbusnote.R
import com.example.nimbusnote.data.model.User
import com.example.nimbusnote.databinding.FragmentProfileBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel
import com.example.nimbusnote.viewModels.MainViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment für die Profilanzeige und -bearbeitung des Benutzers.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding // ViewBinding für das Layout dieses Fragments
    private val viewModel: FirebaseViewModel by activityViewModels() // ViewModel für Firebase-Operationen
    private val mainViewModel: MainViewModel by activityViewModels() // Ein weiteres ViewModel für Hauptoperationen, falls benötigt
    private lateinit var auth: FirebaseAuth // Firebase-Authentifizierungsinstanz

    // ActivityResultLauncher für das Auswählen eines Bildes aus der Galerie
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.uploadImage(it) // Lädt das ausgewählte Bild hoch
            }
        }

    /**
     * Wird aufgerufen, um das Layout des Fragments zu inflatieren.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View vollständig erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bildauswahl starten, wenn auf das Profilbild geklickt wird
        binding.image.setOnClickListener {
            getContent.launch("image/*")
        }

        auth = FirebaseAuth.getInstance()

        // Lädt das Benutzerprofil und zeigt es in den entsprechenden Feldern an
        viewModel.userRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val myProfile = value.toObject(User::class.java)
                myProfile?.let {
                    binding.nameET.setText(it.name)
                    binding.handyNumberET.setText(it.handyNumber)
                    binding.adressET.setText(it.adress)
                    binding.userNameET.setText(it.userName)
                    binding.image.load(it.userImage) // Verwendet Coil zum Laden des Bildes
                }
            }
        }

        // Loggt den Benutzer aus
        binding.Logout.setOnClickListener {
            viewModel.logout()
        }

        // Beobachtet den aktuellen Benutzer und navigiert zum Login-Fragment, wenn der Benutzer abgemeldet ist
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        // Speichert die aktualisierten Benutzerdaten, wenn auf "Speichern" geklickt wird
        binding.saveBTN.setOnClickListener {
            val name = binding.nameET.text.toString()
            val handyNumber = binding.handyNumberET.text.toString()
            val adress = binding.adressET.text.toString()
            val userName = binding.userNameET.text.toString()
            val image = binding.image.drawable.toBitmap().toString()

            if (name.isNotEmpty() && handyNumber.isNotEmpty() && adress.isNotEmpty() && userName.isNotEmpty()) {
                val newUser = User(name, handyNumber, adress, userName, image)
                viewModel.updateUser(newUser)
            }
        }

        viewModel.userProfileImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                binding.image.load(imageUrl) {
                    placeholder(R.drawable.ic_profile_placeholder) // Setzt ein Platzhalterbild
                    error(R.drawable.ic_error) // Setzt ein Fehlerbild
                }
            }
        }
    }
}
