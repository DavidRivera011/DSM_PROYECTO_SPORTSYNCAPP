package com.example.sportsyncapp.ui.solicitudes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.data.repository.SolicitudRepository
import com.example.sportsyncapp.databinding.FragmentSolicitudesAdminBinding
import com.example.sportsyncapp.ui.adapters.SolicitudesAdminAdapter
import java.util.UUID

class SolicitudesAdminFragment : Fragment() {

    private var _binding: FragmentSolicitudesAdminBinding? = null
    private val binding get() = _binding!!

    private val repository = SolicitudRepository()

    private lateinit var adapter: SolicitudesAdminAdapter

    companion object {

        private val ESTADO_APROBADA =
            UUID.fromString("c3d6d66a-9cd5-4d30-b1f2-9adc2b6b6133")

        private val ESTADO_RECHAZADA =
            UUID.fromString("33928a4c-8a28-4d23-9483-05b11f25c1bd")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolicitudesAdminBinding.inflate(
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

        adapter = SolicitudesAdminAdapter(
            emptyList(),
            alAprobar = { id ->
                confirmarDecision(
                    id,
                    ESTADO_APROBADA,
                    "¿Estás seguro de aprobar esta solicitud?",
                    "Aprobar"
                )
            },
            alRechazar = { id ->
                confirmarDecision(
                    id,
                    ESTADO_RECHAZADA,
                    "¿Estás seguro de rechazar esta solicitud?",
                    "Rechazar"
                )
            }
        )

        binding.rvSolicitudesAdmin.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvSolicitudesAdmin.adapter = adapter

        binding.swipeRefreshSolicitudesAdmin.setOnRefreshListener {
            cargarSolicitudes()
        }

        cargarSolicitudes()
    }

    private fun cargarSolicitudes() {
        val vista = _binding ?: return

        vista.swipeRefreshSolicitudesAdmin.isRefreshing = true
        vista.tvSolicitudesAdminVacio.visibility = View.GONE

        repository.obtenerSolicitudesAdmin { resultado ->

            val vistaActual = _binding ?: return@obtenerSolicitudesAdmin

            vistaActual.swipeRefreshSolicitudesAdmin.isRefreshing = false

            if (resultado != null) {

                adapter.actualizarLista(resultado)

                vistaActual.tvSolicitudesAdminVacio.text =
                    "No hay solicitudes registradas."

                vistaActual.tvSolicitudesAdminVacio.visibility =
                    if (resultado.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

            } else {

                adapter.actualizarLista(emptyList())

                vistaActual.tvSolicitudesAdminVacio.text =
                    "No se pudieron cargar las solicitudes."

                vistaActual.tvSolicitudesAdminVacio.visibility =
                    View.VISIBLE

                context?.let {
                    Toast.makeText(
                        it,
                        "Error al consultar las solicitudes.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun confirmarDecision(
        solicitudId: UUID,
        estadoId: UUID,
        mensaje: String,
        accion: String
    ) {
        if (_binding == null) return

        AlertDialog.Builder(requireContext())
            .setTitle("$accion solicitud")
            .setMessage(mensaje)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar") { _, _ ->
                actualizarSolicitud(solicitudId, estadoId)
            }
            .show()
    }

    private fun actualizarSolicitud(
        solicitudId: UUID,
        estadoId: UUID
    ) {
        val vista = _binding ?: return

        vista.swipeRefreshSolicitudesAdmin.isRefreshing = true

        repository.actualizarSolicitud(
            solicitudId = solicitudId,
            estadoId = estadoId
        ) { correcto, mensaje ->

            val vistaActual =
                _binding ?: return@actualizarSolicitud

            vistaActual.swipeRefreshSolicitudesAdmin.isRefreshing = false

            Toast.makeText(
                requireContext(),
                mensaje,
                Toast.LENGTH_LONG
            ).show()

            if (correcto) {
                cargarSolicitudes()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}