package com.example.nimbusnote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nimbusnote.R
import com.example.nimbusnote.databinding.FragmentLoginBinding
import com.example.nimbusnote.viewModels.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment für die Login-Funktionalität, das dem Benutzer ermöglicht, sich anzumelden oder zu registrieren.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding // ViewBinding für das Layout dieses Fragments
    private lateinit var auth: FirebaseAuth // Firebase-Authentifizierungsinstanz
    private val viewModel: FirebaseViewModel by activityViewModels() // ViewModel für Firebase-Operationen

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
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

        auth = FirebaseAuth.getInstance()

        // Setzt das App-Logo.
        binding.appLogo.setImageResource(R.drawable.ic_logo)

        // Wechselt zum Registrierungs-Layout, wenn auf "Registrieren" geklickt wird.
        binding.signUp.setOnClickListener {
            toggleSignUpSignIn(true)
        }

        // Versucht, einen neuen Benutzer zu registrieren, wenn auf "Registrieren" Button geklickt wird.
        binding.signUpBTN.setOnClickListener {
            val email = binding.emailET2.text.toString()
            val pass = binding.passET2.text.toString()
            val userName = binding.userNameET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.register(email, pass, userName)
            } else {
                Toast.makeText(context, "Leere Felder sind nicht erlaubt!", Toast.LENGTH_SHORT).show()
            }
        }

        // Wechselt zum Login-Layout, wenn auf "Anmelden" geklickt wird.
        binding.signIn.setOnClickListener {
            toggleSignUpSignIn(false)
        }

        // Versucht, den Benutzer anzumelden, wenn auf "Anmelden" Button geklickt wird.
        binding.signInBTN.setOnClickListener {
            val email = binding.emailET.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
            } else {
                Toast.makeText(context, "Leere Felder sind nicht erlaubt!", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigiert zum "Passwort vergessen"-Fragment, wenn auf den entsprechenden Text geklickt wird.
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgotFragment)
        }

        // Beobachtet den aktuellen Benutzer und navigiert zum Wetter-Fragment, wenn ein Benutzer angemeldet ist.
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.weatherFragment)
            }
        }
    }

    /**
     * Hilfsfunktion zum Umschalten zwischen Anmelde- und Registrierungs-Layout.
     *
     * @param showSignUp Wenn true, wird das Registrierungs-Layout angezeigt, sonst das Anmelde-Layout.
     */
    private fun toggleSignUpSignIn(showSignUp: Boolean) {
        if (showSignUp) {
            binding.signUp.background = resources.getDrawable(R.drawable.switch_trcks, null)
            binding.signUp.setTextColor(resources.getColor(R.color.black, null))
            binding.signIn.background = null
            binding.signIn.setTextColor(resources.getColor(R.color.pastel_lavender, null))
            binding.singUpLayout.visibility = View.VISIBLE
            binding.logInLayout.visibility = View.GONE
        } else {
            binding.signIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            binding.signIn.setTextColor(resources.getColor(R.color.black, null))
            binding.signUp.background = null
            binding.signUp.setTextColor(resources.getColor(R.color.pastel_lavender, null))
            binding.singUpLayout.visibility = View.GONE
            binding.logInLayout.visibility = View.VISIBLE
        }
    }
}
