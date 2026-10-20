package com.example.sportsyncapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.repository.HomeRepository
import com.example.sportsyncapp.databinding.FragmentHomeBinding
import com.example.sportsyncapp.ui.instituciones.InstitucionesFragment
import com.example.sportsyncapp.ui.equipos.EquiposFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.sportsyncapp.ui.eventos.EventosFragment
import com.example.sportsyncapp.ui.partidos.PartidosFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeRepository = HomeRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutAdmin.visibility = View.GONE
        binding.tvQuickAccessTitle.visibility = View.GONE
        binding.layoutQuickAccess.visibility = View.GONE

        configurarActualizacion()
        configurarAccesosRapidos()
        configurarBotonInscripciones()
        cargarDashboard()
    }

    private fun configurarActualizacion() {
        binding.swipeRefresh.setColorSchemeResources(
            R.color.sportsync_primary_orange
        )

        binding.swipeRefresh.setOnRefreshListener {
            cargarDashboard()
        }
    }

    private fun cargarDashboard() {
        val currentBinding = _binding ?: return

        currentBinding.swipeRefresh.isRefreshing = true
        currentBinding.layoutAdmin.visibility = View.GONE
        currentBinding.tvQuickAccessTitle.visibility = View.GONE
        currentBinding.layoutQuickAccess.visibility = View.GONE

        homeRepository.obtenerResumen { resumen ->
            val bindingActual = _binding ?: return@obtenerResumen

            bindingActual.swipeRefresh.isRefreshing = false

            if (resumen == null) {
                bindingActual.tvWelcomeUser.text = "¡Bienvenido a SportSync!"
                bindingActual.layoutAdmin.visibility = View.GONE
                bindingActual.tvQuickAccessTitle.visibility = View.GONE
                bindingActual.layoutQuickAccess.visibility = View.GONE

                mostrarMensaje("No se pudo verificar el perfil del usuario.")
                return@obtenerResumen
            }

            bindingActual.tvWelcomeUser.text =
                "¡Hola, ${resumen.nombreUsuario}!"

            bindingActual.tvUserSubtitle.text =
                "Consulta torneos, partidos y tus métricas"

            bindingActual.tvStatEvents.text =
                resumen.totalEventos.toString()

            bindingActual.tvStatMatches.text =
                resumen.totalPartidos.toString()

            bindingActual.tvStatInscriptions.text =
                resumen.totalInscripciones.toString()

            bindingActual.tvQuickSportCount.text =
                "Ver disciplinas (${resumen.totalDeportes})"

            if (resumen.esAdministrador) {
                bindingActual.layoutAdmin.visibility = View.VISIBLE
                bindingActual.tvQuickAccessTitle.visibility = View.GONE
                bindingActual.layoutQuickAccess.visibility = View.GONE
            } else {
                bindingActual.layoutAdmin.visibility = View.GONE
                bindingActual.tvQuickAccessTitle.visibility = View.VISIBLE
                bindingActual.layoutQuickAccess.visibility = View.VISIBLE
            }
        }
    }

    private fun configurarAccesosRapidos() {
        binding.cardSports.setOnClickListener {
            abrirSeccion(R.id.nav_sports)
        }

        binding.cardNextEvent.setOnClickListener {
            mostrarMensaje("La agenda estará disponible próximamente.")
        }

        binding.cardPlayers.setOnClickListener {
            mostrarMensaje("El ranking estará disponible próximamente.")
        }

        binding.cardAwards.setOnClickListener {
            mostrarMensaje("Las inscripciones estarán disponibles próximamente.")
        }

        binding.cardAdminInstitutions.setOnClickListener {
            abrirInstituciones()
        }

        binding.cardAdminTeams.setOnClickListener {
            abrirEquipos()
        }

        binding.cardAdminEvents.setOnClickListener {
            abrirEventos()
        }

        binding.cardAdminMatches.setOnClickListener {
            abrirPartidos()
        }

        binding.cardAdminSports.setOnClickListener {
            abrirSeccion(R.id.nav_sports)
        }

        binding.cardAdminInscriptions.setOnClickListener {
            mostrarMensaje("La revisión de inscripciones estará disponible próximamente.")
        }
    }

    private fun configurarBotonInscripciones() {
        binding.btnQuickInscriptions.setOnClickListener {
            mostrarMensaje("Las inscripciones estarán disponibles próximamente.")
        }
    }

    private fun abrirSeccion(itemId: Int) {
        val bottomNavigation = requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation?.selectedItemId = itemId
    }

    private fun abrirInstituciones() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                InstitucionesFragment()
            )
            .addToBackStack("instituciones")
            .commit()
    }

    private fun abrirEquipos() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                EquiposFragment()
            )
            .addToBackStack("equipos")
            .commit()
    }

    private fun abrirEventos() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                EventosFragment()
            )
            .addToBackStack("eventos")
            .commit()
    }

    private fun abrirPartidos() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                PartidosFragment()
            )
            .addToBackStack("partidos")
            .commit()
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
