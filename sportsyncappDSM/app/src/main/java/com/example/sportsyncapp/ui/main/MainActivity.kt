
package com.example.sportsyncapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.sportsyncapp.LoginActivity
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.ActivityMainBinding
import com.example.sportsyncapp.ui.deportes.DeportesFragment
import com.google.firebase.auth.FirebaseAuth
import com.example.sportsyncapp.ui.perfil.PerfilFragment
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.graphics.toColorInt
import com.example.sportsyncapp.ui.eventos.EventosFragment
import com.example.sportsyncapp.ui.home.HomeFragment
import com.example.sportsyncapp.ui.partidos.PartidosFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val usuarioRepository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val cafe = "#4B2217".toColorInt()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosUsuario()
        configurarNavegacion()


        if (savedInstanceState == null) {
            mostrarFragment(HomeFragment())
        }
    }

    private fun cargarDatosUsuario() {
        usuarioRepository.obtenerUsuarioActual { usuario ->
            if (isFinishing || isDestroyed) return@obtenerUsuarioActual

            binding.tvAppTitle.text = "SportSync"

            val esAdministrador = usuario?.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            if (esAdministrador) {
                binding.btnToggleRole.visibility = android.view.View.VISIBLE
                binding.btnToggleRole.text = "Administrador"
            } else {
                binding.btnToggleRole.visibility = android.view.View.GONE
            }
        }
    }


    private fun configurarNavegacion() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_sports -> {
                    mostrarFragment(DeportesFragment())
                    true
                }

                R.id.nav_profile -> {
                    mostrarFragment(PerfilFragment())
                    true
                }

                R.id.nav_home -> {
                    mostrarFragment(HomeFragment())
                    true
                }

                R.id.nav_evento -> {
                    mostrarFragment(EventosFragment())
                    true
                }
                R.id.nav_partidos -> {
                    mostrarFragment(PartidosFragment())
                    true
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Esta sección todavía no está implementada.",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            }
        }
    }


    private fun mostrarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }
}
