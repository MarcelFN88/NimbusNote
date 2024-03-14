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
 * Fragment für die "Passwort vergessen"-Funktionalität, wo Benutzer ihr Passwort zurücksetzen können.
 */
class ForgotFragment : Fragment() {

    private lateinit var binding: FragmentForgotBinding // ViewBinding für das Layout dieses Fragments
    private val viewModel: FirebaseViewModel by activityViewModels() // Verweist auf das ViewModel für Firebase-Operationen

    /**
     * Wird aufgerufen, um das Layout des Fragments zu inflatieren.
     *
     * @param inflater Der LayoutInflater, der das Layout inflatieren kann.
     * @param container Der Container, in den das Layout eingefügt wird.
     * @param savedInstanceState Ein Bundle mit gespeicherten Zustandsdaten.
     * @return Die View für das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View vollständig erstellt wurde. Hier werden Event-Listener registriert.
     *
     * @param view Die erstellte View des Fragments.
     * @param savedInstanceState Ein Bundle mit gespeicherten Zustandsdaten.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Registriert einen OnClickListener für den "Passwort zurücksetzen"-Button.
        binding.resetBTN.setOnClickListener {
            val email = binding.email.text.toString()

            // Überprüft, ob die E-Mail-Adresse nicht leer ist, und fordert dann das Zurücksetzen des Passworts an.
            if (email != "") {
                viewModel.resetPassword(email)
            }

            // Navigiert zurück zum Login-Fragment, nachdem der Zurücksetzen-Vorgang initiiert wurde.
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
