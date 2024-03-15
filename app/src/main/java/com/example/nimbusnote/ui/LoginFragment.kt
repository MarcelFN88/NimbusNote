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
 * Fragment zur Handhabung der Login- und Registrierungsfunktionalität.
 * Ermöglicht dem Benutzer, sich anzumelden, zu registrieren oder das Passwort zurückzusetzen.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.appLogo.setImageResource(R.drawable.ic_logo)

        binding.signUp.setOnClickListener {
            toggleSignUpSignIn(true)
        }

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

        binding.signIn.setOnClickListener {
            toggleSignUpSignIn(false)
        }

        binding.signInBTN.setOnClickListener {
            val email = binding.emailET.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
            } else {
                Toast.makeText(context, "Leere Felder sind nicht erlaubt!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgotFragment)
        }

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.weatherFragment)
            }
        }
    }

    /**
     * Wechselt zwischen der Anzeige der Registrierungs- und Anmeldefenster.
     *
     * @param showSignUp Bestimmt, ob das Registrierungsfenster angezeigt werden soll.
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
