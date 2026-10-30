package com.example.sportsyncapp.ui.registros

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.connector.instance
import com.example.sportsyncapp.databinding.FragmentRegistrosBinding
import com.example.sportsyncapp.ui.main.MainActivity
import com.example.sportsyncapp.ui.solicitudes.SolicitudesAdminFragment
import kotlinx.coroutines.launch

class RegistrosFragment : Fragment() {

    private var _binding: FragmentRegistrosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrosBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        configurarBotones()
        cargarVistaPrevia()
    }

    private fun configurarBotones() {
        binding.btnVerTodasInscripciones.setOnClickListener {
            abrirPantalla(
                InscripcionesFragment(),
                "inscripciones"
            )
        }

        binding.btnVerTodasSolicitudes.setOnClickListener {
            abrirPantalla(
                SolicitudesAdminFragment(),
                "solicitudes_admin"
            )
        }
    }

    private fun abrirPantalla(
        fragment: Fragment,
        identificador: String
    ) {
        val mainActivity = activity as? MainActivity ?: return

        mainActivity.abrirPantallaSecundaria(
            fragment,
            identificador
        )
    }

    private fun cargarVistaPrevia() {
        binding.tvInscripcionesPreview.text =
            "Cargando inscripciones..."

        binding.tvSolicitudesPreview.text =
            "Cargando solicitudes..."

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resultado =
                    DefaultConnector.instance
                        .getInscripcionesAdmin.execute()

                val inscripciones =
                    resultado.data.inscripcioness

                binding.tvInscripcionesPreview.text =
                    if (inscripciones.isEmpty()) {
                        "No hay inscripciones registradas."
                    } else {
                        inscripciones
                            .take(3)
                            .joinToString("\n\n") { inscripcion ->
                                buildString {
                                    append(inscripcion.evento.nombre)
                                    append("\nSolicitante: ")
                                    append(inscripcion.usuarioSolicitante.nombre)
                                    append(" ")
                                    append(inscripcion.usuarioSolicitante.apellido)
                                    append("\nEstado: ")
                                    append(
                                        inscripcion.estado.nombreEstado
                                            ?: "Sin estado"
                                    )
                                    append("\nFecha: ")
                                    append(formatearFecha(inscripcion.fechaInscripcion))
                                }
                            }
                    }

            } catch (e: Exception) {
                Log.e(
                    "RegistrosFragment",
                    "Error al cargar inscripciones",
                    e
                )

                if (_binding != null) {
                    binding.tvInscripcionesPreview.text =
                        "No se pudieron cargar las inscripciones."
                }
            }

            try {
                val resultado =
                    DefaultConnector.instance
                        .getSolicitudesAdmin.execute()

                val solicitudes =
                    resultado.data.solicitudess

                binding.tvSolicitudesPreview.text =
                    if (solicitudes.isEmpty()) {
                        "No hay solicitudes registradas."
                    } else {
                        solicitudes
                            .take(3)
                            .joinToString("\n\n") { solicitud ->
                                buildString {
                                    append(solicitud.usuario.nombre)
                                    append(" ")
                                    append(solicitud.usuario.apellido)
                                    append("\nDeporte: ")
                                    append(solicitud.deporte.nombreDeporte)
                                    append("\nTipo de perfil: ")
                                    append(solicitud.tipoPerfil.nombrePerfil)
                                    append("\nEstado: ")
                                    append(solicitud.estado.nombreEstado)
                                    append("\nFecha: ")
                                    append(formatearFecha(solicitud.fechaSolicitud))
                                }
                            }
                    }

            } catch (e: Exception) {
                Log.e(
                    "RegistrosFragment",
                    "Error al cargar solicitudes",
                    e
                )

                if (_binding != null) {
                    binding.tvSolicitudesPreview.text =
                        "No se pudieron cargar las solicitudes."
                }
            }
        }
    }

    private fun formatearFecha(fecha: Any?): String {
        return try {
            val fechaTexto = fecha.toString()

            val patron = Regex(
                """LocalDate\(year=(\d+), month=(\d+), day=(\d+)\)"""
            )

            val resultado = patron.find(fechaTexto)

            if (resultado != null) {
                val anio = resultado.groupValues[1]
                val mes = resultado.groupValues[2].padStart(2, '0')
                val dia = resultado.groupValues[3].padStart(2, '0')

                "$dia/$mes/$anio"
            } else {
                fechaTexto
            }
        } catch (e: Exception) {
            "Sin fecha"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
