package com.example.sportsyncapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val usuarioRepository = UsuarioRepository()

    private var esperandoSegundoAtras = false
    private var esAdministrador = false

    private val paginasUsuario = listOf(
        R.id.nav_home,
        R.id.nav_evento,
        R.id.nav_sports,
        R.id.nav_partidos,
        R.id.nav_profile
    )

    private val paginasAdministrador = listOf(
        R.id.nav_home,
        R.id.nav_registros,
        R.id.nav_evento,
        R.id.nav_partidos,
        R.id.nav_profile
    )

    private val paginas: List<Int>
        get() = if (esAdministrador) paginasAdministrador else paginasUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarNavegacion()
        configurarBotonAtras()
        cargarDatosUsuario()
    }

    private fun configurarNavegacion() {

        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    if (
                        binding.secondaryFragmentContainer.visibility != View.VISIBLE &&
                        position in paginas.indices
                    ) {
                        val itemId = paginas[position]

                        if (binding.bottomNavigation.selectedItemId != itemId) {
                            binding.bottomNavigation.selectedItemId = itemId
                        }
                    }
                }
            }
        )

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            // Restaurar la selección exclusiva al volver a la navegación principal.
            binding.bottomNavigation.menu.setGroupCheckable(0, true, true)

            if (binding.secondaryFragmentContainer.visibility == View.VISIBLE) {
                binding.viewPager.visibility = View.VISIBLE
                binding.secondaryFragmentContainer.visibility = View.GONE
                binding.viewPager.isUserInputEnabled = true

                supportFragmentManager.popBackStack()
            }

            val position = paginas.indexOf(item.itemId)

            if (position != -1 && binding.viewPager.adapter != null) {
                binding.viewPager.setCurrentItem(position, false)

                // Mantener el color de selección del elemento pulsado.
                item.isChecked = true

                true
            } else {
                false
            }
        }
    }

    private fun cargarDatosUsuario() {
        usuarioRepository.obtenerUsuarioActual { usuario ->

            if (isFinishing || isDestroyed) {
                return@obtenerUsuarioActual
            }

            binding.tvAppTitle.text = "SportSync"

            esAdministrador = usuario?.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            // Cargar el menú según el rol.
            binding.bottomNavigation.menu.clear()

            if (esAdministrador) {
                binding.bottomNavigation.inflateMenu(
                    R.menu.bottom_nav_admin_menu
                )
            } else {
                binding.bottomNavigation.inflateMenu(
                    R.menu.bottom_nav_menu
                )
            }

            // Crear las páginas según el rol.
            binding.viewPager.adapter = MainPagerAdapter(
                activity = this,
                esAdministrador = esAdministrador
            )

            binding.viewPager.setCurrentItem(0, false)
            binding.bottomNavigation.selectedItemId = R.id.nav_home

            if (esAdministrador) {
                binding.btnToggleRole.visibility = View.VISIBLE
                binding.btnToggleRole.text = "Administrador"
            } else {
                binding.btnToggleRole.visibility = View.GONE
            }
        }
    }

    private fun restaurarDashboard() {
        binding.secondaryFragmentContainer.visibility = View.GONE
        binding.viewPager.visibility = View.VISIBLE
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.viewPager.isUserInputEnabled = true

        binding.bottomNavigation.menu.setGroupCheckable(0, true, true)

        val itemId = paginas.getOrElse(binding.viewPager.currentItem) {
            R.id.nav_home
        }

        binding.bottomNavigation.selectedItemId = itemId
    }

    private fun configurarBotonAtras() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (binding.secondaryFragmentContainer.visibility == View.VISIBLE) {
                        if (supportFragmentManager.backStackEntryCount > 0) {
                            supportFragmentManager.popBackStack()
                        }

                        restaurarDashboard()
                        esperandoSegundoAtras = false
                        return
                    }

                    if (binding.viewPager.currentItem != 0) {
                        binding.viewPager.setCurrentItem(0, false)
                        binding.bottomNavigation.selectedItemId = R.id.nav_home
                        esperandoSegundoAtras = false
                        return
                    }

                    if (!esperandoSegundoAtras) {
                        esperandoSegundoAtras = true

                        Toast.makeText(
                            this@MainActivity,
                            "Presiona atrás una vez más para salir",
                            Toast.LENGTH_SHORT
                        ).show()

                        window.decorView.postDelayed({
                            esperandoSegundoAtras = false
                        }, 2000)

                        return
                    }

                    finish()
                }
            }
        )
    }

    fun abrirPantallaSecundaria(
        fragment: Fragment,
        identificador: String
    ) {
        val actual = supportFragmentManager.findFragmentById(
            R.id.secondary_fragment_container
        )

        if (
            binding.secondaryFragmentContainer.visibility == View.VISIBLE &&
            actual?.tag == identificador
        ) {
            return
        }

        binding.viewPager.visibility = View.GONE
        binding.secondaryFragmentContainer.visibility = View.VISIBLE
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.viewPager.isUserInputEnabled = false

        // Quitar la selección visual en pantallas secundarias.
        binding.bottomNavigation.menu.setGroupCheckable(0, false, false)

        for (i in 0 until binding.bottomNavigation.menu.size()) {
            binding.bottomNavigation.menu.getItem(i).isChecked = false
        }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.secondary_fragment_container,
                fragment,
                identificador
            )
            .addToBackStack(identificador)
            .commit()
    }
}
