package com.example.sportsyncapp.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.local.PreferencesManager
import com.example.sportsyncapp.databinding.ActivityMainBinding
import com.example.sportsyncapp.ui.agenda.AgendaFragment
import com.example.sportsyncapp.ui.home.HomeFragment
import com.example.sportsyncapp.ui.matches.MatchesFragment
import com.example.sportsyncapp.ui.profile.ProfileFragment
import com.example.sportsyncapp.ui.sports.SportsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferencesManager(this)

        setupRoleToggle()
        setupBottomNavigation()

        // Pantalla inicial: Home
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }

    private fun setupRoleToggle() {
        updateRoleButtonText()

        binding.btnToggleRole.setOnClickListener {
            // Cambia el rol entre 1 (FAN) y 2 (ADMIN)
            val newRole = if (prefs.userRole == 2) 1 else 2
            prefs.userRole = newRole
            updateRoleButtonText()

            val roleName = if (newRole == 2) "ADMINISTRADOR (Escritura / Modificación)" else "FAN (Solo Lectura)"
            Toast.makeText(this, "Cambiado a: $roleName", Toast.LENGTH_SHORT).show()

            // Recargar el fragmento activo para reflejar permisos de Admin
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment != null) {
                val newFrag = currentFragment::class.java.getDeclaredConstructor().newInstance()
                replaceFragment(newFrag)
            }
        }
    }

    private fun updateRoleButtonText() {
        if (prefs.isAdmin()) {
            binding.btnToggleRole.text = "Modo: ADMIN"
            binding.btnToggleRole.setBackgroundResource(R.drawable.bg_button_orange)
        } else {
            binding.btnToggleRole.text = "Modo: FAN"
            binding.btnToggleRole.setBackgroundResource(R.drawable.bg_badge_pending)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_agenda -> AgendaFragment()
                R.id.nav_sports -> SportsFragment()
                R.id.nav_matches -> MatchesFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            replaceFragment(fragment)
            true
        }
    }

    fun selectTab(tabId: Int) {
        binding.bottomNavigation.selectedItemId = tabId
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
