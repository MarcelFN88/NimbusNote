package com.example.nimbusnote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nimbusnote.R
import com.example.nimbusnote.databinding.FragmentForgotBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel

/**
 * Fragment für die "Passwort vergessen"-Funktionalität, ermöglicht Benutzern das Zurücksetzen ihres Passworts.
 */
class ForgotFragment : Fragment() {

    private lateinit var binding: FragmentForgotBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    /**
     * Erstellt und gibt die View für dieses Fragment zurück.
     * Initialisiert das View Binding für das Layout dieses Fragments.
     *
     * @param inflater Der LayoutInflater, der das Layout inflatieren kann.
     * @param container Der Container, in den das Layout eingefügt wird.
     * @param savedInstanceState Zustandsdaten des Fragments.
     * @return Die erstellte View des Fragments.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen.
     * Setzt den OnClickListener für den "Zurücksetzen"-Button und implementiert die Logik
     * zum Zurücksetzen des Passworts und zur Navigation zurück zum Login-Fragment.
     *
     * @param view Die erstellte View des Fragments.
     * @param savedInstanceState Zustandsdaten des Fragments.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetBTN.setOnClickListener {
            val email = binding.email.text.toString().trim()

            if (email.isNotEmpty()) {
                viewModel.resetPassword(email)
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }
}
