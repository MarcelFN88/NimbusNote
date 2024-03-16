package com.example.nimbusnote

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nimbusnote.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackgroundAnimation()
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)
        val navController = navHost.navController
    }

    private fun setupBackgroundAnimation() {
        val rootView = findViewById<ConstraintLayout>(R.id.main)
        val drawable: AnimationDrawable = rootView.background as AnimationDrawable
        drawable.setExitFadeDuration(1200)
        drawable.start()
    }
}
