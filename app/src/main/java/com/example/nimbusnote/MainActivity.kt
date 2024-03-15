package com.example.nimbusnote

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nimbusnote.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

/**
 * MainActivity dient als der Einstiegspunkt für die Benutzer-Interface-Komponenten der App.
 * Sie initialisiert Firebase, verwaltet die Navigation zwischen den verschiedenen Fragmenten und
 * handhabt die Standortdienste zur Erfassung des letzten bekannten Standorts des Benutzers.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeFirebase()
        initializeLocationClient()
        setupBackgroundAnimation()
        setupNavigation()
    }

    /**
     * Initialisiert Firebase und die Authentifizierungsdienste.
     */
    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
    }

    /**
     * Initialisiert den FusedLocationProviderClient für den Zugriff auf die Standortdienste.
     */
    private fun initializeLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    /**
     * Richtet die Hintergrundanimation für die Hauptaktivität ein.
     */
    private fun setupBackgroundAnimation() {
        val rootView = findViewById<ConstraintLayout>(R.id.rootView)
        val drawable: AnimationDrawable = rootView.background as AnimationDrawable
        drawable.setExitFadeDuration(1200)
        drawable.start()
    }

    /**
     * Konfiguriert die Navigation und das BottomNavigationView.
     */
    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)
        val navController = navHost.navController

        // Passt die Sichtbarkeit der BottomNavigationView basierend auf der aktuellen Destination an.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.chatFragment, R.id.loginFragment -> binding.bottomNavigationView.visibility = BottomNavigationView.GONE
                else -> binding.bottomNavigationView.visibility = BottomNavigationView.VISIBLE
            }
        }
    }

    /**
     * Ermittelt den letzten bekannten Standort des Benutzers und loggt die Breiten- und Längengrade.
     */
    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                Log.d("MainActivity", "Aktueller Standort: Lat = ${it.latitude}, Lon = ${it.longitude}")
            }
        }
    }
}
