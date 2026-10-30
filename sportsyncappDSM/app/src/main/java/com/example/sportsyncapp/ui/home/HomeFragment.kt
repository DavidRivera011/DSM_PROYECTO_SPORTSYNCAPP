package com.example.sportsyncapp.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Partido
import com.example.sportsyncapp.data.repository.HomeRepository
import com.example.sportsyncapp.data.repository.PartidosRepository
import com.example.sportsyncapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.example.sportsyncapp.ui.main.MainActivity
import com.example.sportsyncapp.ui.solicitudes.SolicitudesFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeRepository = HomeRepository()
    private val partidosRepository = PartidosRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarActualizacion()
        configurarAccesosRapidos()
        configurarBotonesPartidos()
        cargarDashboard()
        cargarPartidoDestacado()
    }

    // ACTUALIZAR EL INICIO AL DESLIZAR HACIA ABAJO
    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarActualizacion() {
        binding.swipeRefresh.setColorSchemeResources(
            R.color.sportsync_primary_orange
        )

        binding.swipeRefresh.setOnRefreshListener {
            cargarDashboard()
            cargarPartidoDestacado()
        }
    }

    // CARGAR ESTADÍSTICAS Y DATOS DEL USUARIO
    private fun cargarDashboard() {
        val currentBinding = _binding ?: return

        currentBinding.swipeRefresh.isRefreshing = true

        homeRepository.obtenerResumen { resumen ->

            val bindingActual = _binding
                ?: return@obtenerResumen

            bindingActual.swipeRefresh.isRefreshing = false

            if (resumen == null) {
                bindingActual.tvWelcomeUser.text =
                    "¡Bienvenido a SportSync!"

                bindingActual.tvUserSubtitle.text =
                    "No se pudo cargar tu información."

                bindingActual.tvStatEvents.text = "—"
                bindingActual.tvStatMatches.text = "—"
                bindingActual.tvStatInscriptions.text = "—"

                bindingActual.tvQuickSportCount.text =
                    "Ver disciplinas"

                mostrarMensaje(
                    "No se pudo cargar el resumen del usuario."
                )

                return@obtenerResumen
            }

            bindingActual.tvWelcomeUser.text =
                "¡Hola, ${resumen.nombreUsuario}!"

            bindingActual.tvUserSubtitle.text =
                "Encuentra eventos, consulta partidos y participa."

            bindingActual.tvStatEvents.text =
                resumen.totalEventos.toString()

            bindingActual.tvStatMatches.text =
                resumen.totalPartidos.toString()

            bindingActual.tvStatInscriptions.text =
                resumen.totalInscripciones.toString()

            bindingActual.tvQuickSportCount.text =
                "Ver disciplinas (${resumen.totalDeportes})"

            bindingActual.tvQuickEventCount.text =
                "${resumen.totalEventos} eventos disponibles"
        }
    }

    // CONFIGURAR ACCESOS RÁPIDOS
    private fun configurarAccesosRapidos() {

        // Explorar eventos
        binding.cardNextEvent.setOnClickListener {
            abrirSeccion(R.id.nav_evento)
        }

        binding.cardQuickEvents.setOnClickListener {
            abrirSeccion(R.id.nav_evento)
        }

        // Consultar deportes
        binding.cardSports.setOnClickListener {
            abrirSeccion(R.id.nav_sports)
        }

        // Consultar partidos
        binding.cardPlayers.setOnClickListener {
            abrirSeccion(R.id.nav_partidos)
        }

        // Abrir la pestaña de perfil
        binding.cardAwards.setOnClickListener {
            abrirSeccion(R.id.nav_profile)
        }
    }

    // BOTONES DE LA SECCIÓN DE PARTIDOS E INSCRIPCIONES
    private fun configurarBotonesPartidos() {

        binding.tvSeeAllMatches.setOnClickListener {
            abrirSeccion(R.id.nav_partidos)
        }

        binding.btnOpenMatches.setOnClickListener {
            abrirSeccion(R.id.nav_partidos)
        }

        binding.cardFeaturedMatch.setOnClickListener {
            abrirSeccion(R.id.nav_partidos)
        }

        binding.btnQuickInscriptions.setOnClickListener {
            abrirPantallaSecundaria(
                SolicitudesFragment(),
                "solicitudes"
            )
        }
    }

    // CARGAR PARTIDO EN VIVO O PRÓXIMO PARTIDO
    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarPartidoDestacado() {

        partidosRepository.obtenerPartidos { partidos ->

            val vista = _binding
                ?: return@obtenerPartidos

            if (partidos.isNullOrEmpty()) {
                mostrarEstadoSinPartidos()
                return@obtenerPartidos
            }

            // Priorizar un partido que esté en curso.
            val enVivo = partidos.firstOrNull { partido ->
                partido.estadoNombre.contains(
                    "en vivo",
                    ignoreCase = true
                ) || partido.estadoNombre.contains(
                    "en curso",
                    ignoreCase = true
                )
            }

            val hoy = LocalDate.now()
            val formatoFecha = DateTimeFormatter.ISO_LOCAL_DATE

            // Buscar partidos de hoy o de fechas posteriores.
            val proximos = partidos
                .filter { partido ->
                    partido.fecha.isNotBlank() &&
                            !partido.estadoNombre.contains(
                                "finalizado",
                                ignoreCase = true
                            ) &&
                            runCatching {
                                !LocalDate.parse(
                                    partido.fecha,
                                    formatoFecha
                                ).isBefore(hoy)
                            }.getOrDefault(false)
                }
                .sortedWith(
                    compareBy<Partido> { partido ->
                        runCatching {
                            LocalDate.parse(
                                partido.fecha,
                                formatoFecha
                            )
                        }.getOrNull()
                    }.thenBy { partido ->
                        runCatching {
                            LocalTime.parse(partido.hora)
                        }.getOrNull()
                    }
                )

            val destacado = enVivo ?: proximos.firstOrNull()

            if (destacado == null) {
                vista.tvFeaturedMatchStatus.text =
                    "SIN PRÓXIMOS PARTIDOS"

                vista.tvFeaturedMatchTourney.text =
                    "No hay encuentros próximos programados."

                vista.tvFeaturedLocal.text = "—"
                vista.tvFeaturedScore.text = "VS"
                vista.tvFeaturedVisitor.text = "—"

                vista.tvFeaturedMatchDate.text =
                    "Consulta la sección de partidos para ver más."

                return@obtenerPartidos
            }

            val estaEnVivo = enVivo != null &&
                    destacado.id == enVivo.id

            vista.tvFeaturedMatchStatus.text =
                if (estaEnVivo) {
                    "● EN VIVO"
                } else {
                    "PRÓXIMO PARTIDO"
                }

            vista.tvFeaturedMatchTourney.text =
                destacado.eventoNombre.ifBlank {
                    "Encuentro deportivo"
                }

            vista.tvFeaturedLocal.text =
                destacado.equipoLocalNombre

            vista.tvFeaturedVisitor.text =
                destacado.equipoVisitanteNombre

            vista.tvFeaturedScore.text =
                if (estaEnVivo) {
                    "${destacado.marcadorLocal} - " +
                            "${destacado.marcadorVisitante}"
                } else {
                    "VS"
                }

            val hora = destacado.hora.ifBlank {
                "Hora por confirmar"
            }

            vista.tvFeaturedMatchDate.text =
                "${destacado.fecha} · $hora"
        }
    }

    private fun abrirPantallaSecundaria(
        fragment: Fragment,
        identificador: String
    ) {
        val mainActivity = activity as? MainActivity ?: return

        mainActivity.abrirPantallaSecundaria(
            fragment,
            identificador
        )
    }

    // MOSTRAR ESTADO VACÍO
    private fun mostrarEstadoSinPartidos() {
        val vista = _binding ?: return

        vista.tvFeaturedMatchStatus.text =
            "SIN ENCUENTROS"

        vista.tvFeaturedMatchTourney.text =
            "Todavía no hay partidos registrados."

        vista.tvFeaturedLocal.text = "—"
        vista.tvFeaturedScore.text = "VS"
        vista.tvFeaturedVisitor.text = "—"

        vista.tvFeaturedMatchDate.text =
            "Los próximos encuentros aparecerán aquí."
    }

    // NAVEGAR A UNA PESTAÑA
    private fun abrirSeccion(itemId: Int) {
        val bottomNavigation =
            activity?.findViewById<BottomNavigationView>(
                R.id.bottom_navigation
            )

        if (bottomNavigation == null) {
            mostrarMensaje("No se pudo abrir la sección.")
            return
        }

        bottomNavigation.selectedItemId = itemId
    }

    // MOSTRAR MENSAJES
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
