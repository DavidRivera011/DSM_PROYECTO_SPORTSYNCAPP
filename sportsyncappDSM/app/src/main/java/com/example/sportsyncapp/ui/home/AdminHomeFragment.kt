package com.example.sportsyncapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportsyncapp.R
import com.example.sportsyncapp.databinding.FragmentAdminHomeBinding
import com.example.sportsyncapp.ui.equipos.EquiposFragment
import com.example.sportsyncapp.ui.instituciones.InstitucionesFragment
import com.example.sportsyncapp.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.sportsyncapp.ui.deportes.DeportesFragment

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarNavegacion()
    }

    private fun configurarNavegacion() {

        // EVENTOS
        binding.cardAdminEvents.setOnClickListener {
            abrirSeccion(R.id.nav_evento)
        }

        // PARTIDOS
        binding.cardAdminMatches.setOnClickListener {
            abrirSeccion(R.id.nav_partidos)
        }

        // DEPORTES
        binding.cardAdminSports.setOnClickListener {
            abrirDeportes()
        }

        // INSTITUCIONES
        binding.cardAdminInstitutions.setOnClickListener {
            abrirInstituciones()
        }

        // EQUIPOS
        binding.cardAdminTeams.setOnClickListener {
            abrirEquipos()
        }

        // SOLICITUDES
        binding.cardAdminInscriptions.setOnClickListener {
            mostrarMensaje(
                "La revisión de inscripciones estará disponible próximamente."
            )
        }
    }

    private fun abrirSeccion(itemId: Int) {
        val bottomNavigation = requireActivity()
            .findViewById<BottomNavigationView>(
                R.id.bottom_navigation
            )

        val item = bottomNavigation.menu.findItem(itemId)

        if (item == null) {
            mostrarMensaje(
                "La sección no está configurada en la navegación."
            )
            return
        }

        bottomNavigation.selectedItemId = itemId
    }

    private fun abrirDeportes() {
        val mainActivity = activity as? MainActivity

        if (mainActivity == null) {
            mostrarMensaje("No se pudo abrir Deportes.")
            return
        }

        mainActivity.abrirPantallaSecundaria(
            DeportesFragment(),
            "deportes"
        )
    }

    private fun abrirInstituciones() {
        val mainActivity = activity as? MainActivity

        if (mainActivity == null) {
            mostrarMensaje("No se pudo abrir Instituciones.")
            return
        }

        mainActivity.abrirPantallaSecundaria(
            InstitucionesFragment(),
            "instituciones"
        )
    }

    private fun abrirEquipos() {
        val mainActivity = activity as? MainActivity

        if (mainActivity == null) {
            mostrarMensaje("No se pudo abrir Equipos.")
            return
        }

        mainActivity.abrirPantallaSecundaria(
            EquiposFragment(),
            "equipos"
        )
    }

    private fun mostrarMensaje(mensaje: String) {
        context?.let {
            Toast.makeText(
                it,
                mensaje,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}