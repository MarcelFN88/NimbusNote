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


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackgroundAnimation()
        setupNavigation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)
        val navController = navHost.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "Navigiere zu Destination mit ID: ${destination.id}")
            when (destination.id) {
                R.id.chatFragment,
                R.id.loginFragment -> {
                    binding.bottomNavigationView.visibility = BottomNavigationView.GONE
                }

                else -> {
                    binding.bottomNavigationView.visibility = BottomNavigationView.VISIBLE
                }
            }
        }
    }

    private fun setupBackgroundAnimation() {
        val rootView = findViewById<ConstraintLayout>(R.id.main)
        val drawable: AnimationDrawable = rootView.background as AnimationDrawable
        drawable.setExitFadeDuration(1200)
        drawable.start()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
