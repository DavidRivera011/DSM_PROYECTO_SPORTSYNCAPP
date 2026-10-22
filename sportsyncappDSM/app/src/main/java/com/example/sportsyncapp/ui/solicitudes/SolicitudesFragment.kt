package com.example.sportsyncapp.ui.solicitudes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.data.model.Solicitud
import com.example.sportsyncapp.data.repository.SolicitudRepository
import com.example.sportsyncapp.databinding.FragmentSolicitudesBinding
import com.example.sportsyncapp.ui.adapters.SolicitudesAdapter
import java.util.UUID

class SolicitudesFragment : Fragment() {

    private var _binding: FragmentSolicitudesBinding? = null
    private val binding get() = _binding!!

    private val repository = SolicitudRepository()
    private lateinit var adapter: SolicitudesAdapter

    private var deportes: List<Pair<UUID, String>> = emptyList()
    private var tiposPerfil: List<Pair<UUID, String>> = emptyList()
    private var instituciones: List<Pair<UUID, String>> = emptyList()
    private var equipos: List<Pair<UUID, String>> = emptyList()

    private var catalogosCargados = 0
    private var catalogosConError = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolicitudesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SolicitudesAdapter(emptyList())

        binding.rvSolicitudes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvSolicitudes.adapter = adapter

        binding.swipeRefreshSolicitudes.setOnRefreshListener {
            cargarSolicitudes()
        }

        binding.btnCrearSolicitud.setOnClickListener {
            enviarSolicitud()
        }

        cargarSolicitudes()
        cargarCatalogos()
    }

    private fun cargarSolicitudes() {
        val bindingActual = _binding ?: return

        bindingActual.swipeRefreshSolicitudes.isRefreshing = true
        bindingActual.tvSolicitudesVacio.visibility = View.GONE

        repository.obtenerSolicitudes { resultado ->
            val vista = _binding ?: return@obtenerSolicitudes

            vista.swipeRefreshSolicitudes.isRefreshing = false

            if (resultado != null) {
                adapter.actualizarLista(resultado)

                vista.tvSolicitudesVacio.text =
                    "No tienes solicitudes registradas."

                vista.tvSolicitudesVacio.visibility =
                    if (resultado.isEmpty()) View.VISIBLE else View.GONE
            } else {
                adapter.actualizarLista(emptyList())

                vista.tvSolicitudesVacio.text =
                    "No se pudieron cargar las solicitudes."

                vista.tvSolicitudesVacio.visibility = View.VISIBLE

                Toast.makeText(
                    requireContext(),
                    "Error al consultar tus solicitudes.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cargarCatalogos() {
        catalogosCargados = 0
        catalogosConError = false

        repository.obtenerDeportes { resultado ->
            if (_binding == null) return@obtenerDeportes

            if (resultado == null) {
                catalogosConError = true
            } else {
                deportes = resultado
                configurarSpinner(
                    binding.spDeportesSolicitud,
                    listOf("Selecciona un deporte") + deportes.map { it.second }
                )
            }

            catalogoCargado()
        }

        repository.obtenerTiposPerfil { resultado ->
            if (_binding == null) return@obtenerTiposPerfil

            if (resultado == null) {
                catalogosConError = true
            } else {
                tiposPerfil = resultado
                configurarSpinner(
                    binding.spTipoPerfilSolicitud,
                    listOf("Selecciona un tipo de perfil") +
                            tiposPerfil.map { it.second }
                )
            }

            catalogoCargado()
        }

        repository.obtenerInstituciones { resultado ->
            if (_binding == null) return@obtenerInstituciones

            if (resultado == null) {
                catalogosConError = true
            } else {
                instituciones = resultado

                configurarSpinner(
                    binding.spInstitucionSolicitud,
                    listOf("Sin institución") +
                            instituciones.map { it.second }
                )
            }

            catalogoCargado()
        }

        repository.obtenerEquipos { resultado ->
            if (_binding == null) return@obtenerEquipos

            if (resultado == null) {
                catalogosConError = true
            } else {
                equipos = resultado

                configurarSpinner(
                    binding.spEquipoSolicitud,
                    listOf("Sin equipo") + equipos.map { it.second }
                )
            }

            catalogoCargado()
        }
    }

    private fun catalogoCargado() {
        catalogosCargados++

        if (catalogosCargados == 4 && catalogosConError) {
            Toast.makeText(
                requireContext(),
                "No se pudieron cargar todos los catálogos.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun configurarSpinner(
        spinner: android.widget.Spinner,
        opciones: List<String>
    ) {
        val contexto = context ?: return

        val spinnerAdapter = ArrayAdapter(
            contexto,
            android.R.layout.simple_spinner_item,
            opciones
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = spinnerAdapter
    }

    private fun enviarSolicitud() {
        val vista = _binding ?: return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Toast.makeText(
                requireContext(),
                "Esta operación requiere Android 8.0 o superior.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val indiceDeporte = vista.spDeportesSolicitud.selectedItemPosition
        val indiceTipo = vista.spTipoPerfilSolicitud.selectedItemPosition
        val indiceInstitucion =
            vista.spInstitucionSolicitud.selectedItemPosition
        val indiceEquipo = vista.spEquipoSolicitud.selectedItemPosition

        if (deportes.isEmpty() || indiceDeporte <= 0 ||
            indiceDeporte > deportes.size
        ) {
            Toast.makeText(
                requireContext(),
                "Selecciona un deporte.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (tiposPerfil.isEmpty() || indiceTipo <= 0 ||
            indiceTipo > tiposPerfil.size
        ) {
            Toast.makeText(
                requireContext(),
                "Selecciona un tipo de perfil.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val deporteSeleccionado = deportes[indiceDeporte - 1].first
        val tipoPerfilSeleccionado = tiposPerfil[indiceTipo - 1].first

        val institucionSeleccionada: UUID? =
            if (indiceInstitucion > 0 &&
                indiceInstitucion <= instituciones.size
            ) {
                instituciones[indiceInstitucion - 1].first
            } else {
                null
            }

        val equipoSeleccionado: UUID? =
            if (indiceEquipo > 0 && indiceEquipo <= equipos.size) {
                equipos[indiceEquipo - 1].first
            } else {
                null
            }

        val nombreDeportivo = vista.etNombreDeportivo.text
            ?.toString()
            ?.trim()
            ?.takeIf { it.isNotBlank() }

        vista.btnCrearSolicitud.isEnabled = false

        repository.crearSolicitud(
            deporteId = deporteSeleccionado,
            tipoPerfilId = tipoPerfilSeleccionado,
            institucionId = institucionSeleccionada,
            equipoId = equipoSeleccionado,
            nombreDeportivo = nombreDeportivo,
            fotoDeportiva = null
        ) { correcto, mensaje ->

            val vistaActual = _binding ?: return@crearSolicitud

            vistaActual.btnCrearSolicitud.isEnabled = true

            Toast.makeText(
                requireContext(),
                mensaje,
                Toast.LENGTH_LONG
            ).show()

            if (correcto) {
                vistaActual.etNombreDeportivo.text?.clear()
                vistaActual.spDeportesSolicitud.setSelection(0)
                vistaActual.spTipoPerfilSolicitud.setSelection(0)
                vistaActual.spInstitucionSolicitud.setSelection(0)
                vistaActual.spEquipoSolicitud.setSelection(0)

                cargarSolicitudes()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
