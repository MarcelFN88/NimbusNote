package com.example.nimbusnote.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
 * Das Fragment `ProfileFragment` ermöglicht es dem Benutzer, sein Profil zu betrachten und zu bearbeiten.
 * Dies beinhaltet die Möglichkeit, Profilinformationen wie Namen, Telefonnummer, Adresse und Benutzernamen zu aktualisieren.
 * Zusätzlich kann der Benutzer ein Profilbild auswählen und hochladen. Das Fragment nutzt Firebase zur Authentifizierung
 * und Speicherung der Benutzerdaten sowie Coil zur Bildverarbeitung.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.image.load(it) // Zeigt das ausgewählte Bild direkt an
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.image.setOnClickListener {
            getContent.launch("image/*")
        }

        auth = FirebaseAuth.getInstance()

        viewModel.userRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val myProfile = value.toObject(User::class.java)
                myProfile?.let {
                    binding.nameET.setText(it.name)
                    binding.handyNumberET.setText(it.handyNumber)
                    binding.adressET.setText(it.adress)
                    binding.userNameET.setText(it.userName)
                    if (it.userImage.isNotEmpty()) {
                        binding.image.load(it.userImage) // Lädt das Profilbild, falls vorhanden
                    }
                }
            }
        }

        binding.Logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        binding.saveBTN.setOnClickListener {
            val name = binding.nameET.text.toString()
            val handyNumber = binding.handyNumberET.text.toString()
            val adress = binding.adressET.text.toString()
            val userName = binding.userNameET.text.toString()

            if (name.isNotEmpty() && handyNumber.isNotEmpty() && adress.isNotEmpty() && userName.isNotEmpty()) {
                val newUser = User(name, handyNumber, adress, userName, "")
                viewModel.updateUser(newUser)
                selectedImageUri?.let { viewModel.uploadImage(it) } // Lädt das neue Bild hoch, falls ausgewählt
            }
        }

        viewModel.userProfileImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                binding.image.load(imageUrl) {
                    placeholder(R.drawable.ic_profile_placeholder)
                    error(R.drawable.ic_error)
                }
            }
        }
    }
}
