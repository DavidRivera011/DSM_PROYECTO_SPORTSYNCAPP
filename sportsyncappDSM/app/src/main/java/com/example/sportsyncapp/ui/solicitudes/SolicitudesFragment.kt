package com.example.sportsyncapp.ui.solicitudes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Solicitud
import com.example.sportsyncapp.data.repository.SolicitudRepository
import com.example.sportsyncapp.databinding.FragmentSolicitudesBinding
import com.example.sportsyncapp.ui.adapters.SolicitudesAdapter
import com.google.android.material.button.MaterialButton
import java.util.UUID
import android.util.Log

class SolicitudesFragment : Fragment() {

    private var _binding: FragmentSolicitudesBinding? = null
    private val binding get() = _binding!!

    private val repository = SolicitudRepository()
    private lateinit var adapter: SolicitudesAdapter

    // Catálogos
    private var todosLosDeportes: List<Pair<UUID, String>> = emptyList()
    private var deportes: List<Pair<UUID, String>> = emptyList()
    private var tiposPerfil: List<Pair<UUID, String>> = emptyList()
    private var instituciones: List<Pair<UUID, String>> = emptyList()
    private var equipos: List<Pair<UUID, String>> = emptyList()

    // Solicitudes del usuario
    private var solicitudesActuales: List<Solicitud> = emptyList()
    private var deportesAprobadosIds: Set<UUID> = emptySet()

    // Estado de carga
    private var catalogosCargados = 0
    private var catalogosConError = false
    private var catalogosListos = false
    private var solicitudesCargadas = false

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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SolicitudesAdapter(emptyList())

        binding.rvSolicitudes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvSolicitudes.adapter = adapter

        binding.btnNuevaSolicitud.visibility = View.GONE

        binding.swipeRefreshSolicitudes.setOnRefreshListener {
            cargarSolicitudes()
        }

        binding.btnNuevaSolicitud.setOnClickListener {
            mostrarDialogoNuevaSolicitud()
        }

        cargarSolicitudes()
        cargarCatalogos()
    }

    // ----------------------------------------------------
    // CARGAR SOLICITUDES DEL USUARIO
    // ----------------------------------------------------

    private fun cargarSolicitudes() {
        val vista = _binding ?: return

        solicitudesCargadas = false

        vista.progressSolicitudes.visibility = View.VISIBLE
        vista.rvSolicitudes.visibility = View.GONE
        vista.tvSolicitudesVacio.visibility = View.GONE
        vista.btnNuevaSolicitud.visibility = View.GONE

        repository.obtenerSolicitudes { resultado ->

            val vistaActual = _binding ?: return@obtenerSolicitudes

            vistaActual.progressSolicitudes.visibility = View.GONE
            vistaActual.swipeRefreshSolicitudes.isRefreshing = false

            if (resultado != null) {

                solicitudesActuales = resultado
                solicitudesCargadas = true

                adapter.actualizarLista(resultado)

                // Identificar los deportes que ya fueron aprobados.
                deportesAprobadosIds = resultado
                    .filter {
                        it.estadoNombre.equals(
                            "Aprobada",
                            ignoreCase = true
                        )
                    }
                    .map { it.deporteId }
                    .toSet()

                // Actualizar los deportes disponibles.
                actualizarDeportesDisponibles()

                vistaActual.rvSolicitudes.visibility =
                    if (resultado.isNotEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                vistaActual.tvSolicitudesVacio.text =
                    "No tienes solicitudes registradas."

                vistaActual.tvSolicitudesVacio.visibility =
                    if (resultado.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                // Actualizar la visibilidad del botón.
                actualizarVisibilidadBoton()

            } else {

                solicitudesCargadas = false

                vistaActual.btnNuevaSolicitud.visibility = View.GONE
                vistaActual.rvSolicitudes.visibility = View.GONE

                vistaActual.tvSolicitudesVacio.text =
                    "No se pudieron cargar tus solicitudes. Intenta nuevamente."

                vistaActual.tvSolicitudesVacio.visibility = View.VISIBLE

                Toast.makeText(
                    requireContext(),
                    "Error al consultar tus solicitudes.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // ----------------------------------------------------
    // CARGAR CATÁLOGOS
    // ----------------------------------------------------

    private fun cargarCatalogos() {

        catalogosCargados = 0
        catalogosConError = false
        catalogosListos = false

        repository.obtenerDeportes { resultado ->
            if (_binding == null) return@obtenerDeportes

            if (resultado == null) {
                catalogosConError = true
            } else {
                todosLosDeportes = resultado
                actualizarDeportesDisponibles()
            }

            catalogoCargado()
        }

        repository.obtenerTiposPerfil { resultado ->
            if (_binding == null) return@obtenerTiposPerfil

            if (resultado == null) {
                catalogosConError = true
            } else {
                tiposPerfil = resultado
            }

            catalogoCargado()
        }

        repository.obtenerInstituciones { resultado ->
            if (_binding == null) return@obtenerInstituciones

            if (resultado == null) {
                catalogosConError = true
            } else {
                instituciones = resultado
            }

            catalogoCargado()
        }

        repository.obtenerEquipos { resultado ->
            if (_binding == null) return@obtenerEquipos

            if (resultado == null) {
                catalogosConError = true
            } else {
                equipos = resultado
            }

            catalogoCargado()
        }
    }

    private fun catalogoCargado() {
        catalogosCargados++

        if (catalogosCargados == 4) {

            catalogosListos = !catalogosConError

            if (catalogosConError) {
                Toast.makeText(
                    requireContext(),
                    "No se pudieron cargar todos los catálogos.",
                    Toast.LENGTH_LONG
                ).show()
            }

            actualizarVisibilidadBoton()
        }
    }

    // ----------------------------------------------------
    // FILTRAR DEPORTES YA APROBADOS
    // ----------------------------------------------------

    private fun actualizarDeportesDisponibles() {

        deportes = todosLosDeportes.filter { deporte ->
            deporte.first !in deportesAprobadosIds
        }

        actualizarVisibilidadBoton()
    }

    // ----------------------------------------------------
    // CONTROLAR VISIBILIDAD DEL BOTÓN
    // ----------------------------------------------------

    private fun actualizarVisibilidadBoton() {
        val vista = _binding ?: return

        val tienePendiente = solicitudesActuales.any {
            it.estadoNombre.equals("Pendiente", ignoreCase = true)
        }

        val puedeCrearSolicitud =
            solicitudesCargadas &&
                    catalogosListos &&
                    !catalogosConError &&
                    !tienePendiente &&
                    deportes.isNotEmpty()

        Log.d("SOLICITUDES_DEBUG", """
        Solicitudes cargadas: $solicitudesCargadas
        Catálogos listos: $catalogosListos
        Error en catálogos: $catalogosConError
        Solicitud pendiente: $tienePendiente
        Total de deportes: ${todosLosDeportes.size}
        Deportes disponibles: ${deportes.size}
        Total de solicitudes: ${solicitudesActuales.size}
        Mostrar botón: $puedeCrearSolicitud
    """.trimIndent())

        vista.btnNuevaSolicitud.visibility =
            if (puedeCrearSolicitud) View.VISIBLE else View.GONE
    }

    // ----------------------------------------------------
    // CONFIGURAR SPINNER
    // ----------------------------------------------------

    private fun configurarSpinner(
        spinner: Spinner,
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

    // ----------------------------------------------------
    // MOSTRAR DIÁLOGO DE NUEVA SOLICITUD
    // ----------------------------------------------------

    private fun mostrarDialogoNuevaSolicitud() {

        if (!solicitudesCargadas ||
            !catalogosListos ||
            catalogosConError
        ) {
            Toast.makeText(
                requireContext(),
                "Espera a que se carguen los datos.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (deportes.isEmpty() || tiposPerfil.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "No hay deportes o tipos de perfil disponibles.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val vistaDialogo = layoutInflater.inflate(
            R.layout.dialog_nueva_solicitud,
            null
        )

        val dialogo = AlertDialog.Builder(requireContext())
            .setView(vistaDialogo)
            .create()

        val spDeporte =
            vistaDialogo.findViewById<Spinner>(
                R.id.spDeportesSolicitud
            )

        val spTipoPerfil =
            vistaDialogo.findViewById<Spinner>(
                R.id.spTipoPerfilSolicitud
            )

        val spInstitucion =
            vistaDialogo.findViewById<Spinner>(
                R.id.spInstitucionSolicitud
            )

        val spEquipo =
            vistaDialogo.findViewById<Spinner>(
                R.id.spEquipoSolicitud
            )

        val btnCrear =
            vistaDialogo.findViewById<MaterialButton>(
                R.id.btnCrearSolicitud
            )

        val btnCancelar =
            vistaDialogo.findViewById<MaterialButton>(
                R.id.btnCancelarSolicitud
            )

        configurarSpinner(
            spDeporte,
            listOf("Selecciona un deporte") +
                    deportes.map { it.second }
        )

        configurarSpinner(
            spTipoPerfil,
            listOf("Selecciona un tipo de perfil") +
                    tiposPerfil.map { it.second }
        )

        configurarSpinner(
            spInstitucion,
            listOf("Sin institución") +
                    instituciones.map { it.second }
        )

        configurarSpinner(
            spEquipo,
            listOf("Sin equipo") +
                    equipos.map { it.second }
        )

        btnCrear.setOnClickListener {
            enviarSolicitud(
                vistaDialogo,
                dialogo
            )
        }

        btnCancelar.setOnClickListener {
            dialogo.dismiss()
        }

        dialogo.setOnShowListener {
            dialogo.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.92).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        dialogo.show()
    }

    // ----------------------------------------------------
    // ENVIAR SOLICITUD
    // ----------------------------------------------------

    @RequiresApi(Build.VERSION_CODES.O)
    private fun enviarSolicitud(
        vista: View,
        dialogo: AlertDialog
    ) {

        val spDeporte =
            vista.findViewById<Spinner>(
                R.id.spDeportesSolicitud
            )

        val spTipo =
            vista.findViewById<Spinner>(
                R.id.spTipoPerfilSolicitud
            )

        val spInstitucion =
            vista.findViewById<Spinner>(
                R.id.spInstitucionSolicitud
            )

        val spEquipo =
            vista.findViewById<Spinner>(
                R.id.spEquipoSolicitud
            )

        val etNombre =
            vista.findViewById<EditText>(
                R.id.etNombreDeportivo
            )

        val btnCrear =
            vista.findViewById<MaterialButton>(
                R.id.btnCrearSolicitud
            )

        val indiceDeporte = spDeporte.selectedItemPosition
        val indiceTipo = spTipo.selectedItemPosition
        val indiceInstitucion = spInstitucion.selectedItemPosition
        val indiceEquipo = spEquipo.selectedItemPosition

        if (indiceDeporte <= 0 || indiceDeporte > deportes.size) {
            Toast.makeText(
                requireContext(),
                "Selecciona un deporte.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (indiceTipo <= 0 || indiceTipo > tiposPerfil.size) {
            Toast.makeText(
                requireContext(),
                "Selecciona un tipo de perfil.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val deporteSeleccionado =
            deportes[indiceDeporte - 1].first

        val tipoPerfilSeleccionado =
            tiposPerfil[indiceTipo - 1].first

        val institucionSeleccionada: UUID? =
            if (indiceInstitucion > 0 &&
                indiceInstitucion <= instituciones.size
            ) {
                instituciones[indiceInstitucion - 1].first
            } else {
                null
            }

        val equipoSeleccionado: UUID? =
            if (indiceEquipo > 0 &&
                indiceEquipo <= equipos.size
            ) {
                equipos[indiceEquipo - 1].first
            } else {
                null
            }

        val nombreDeportivo = etNombre.text
            ?.toString()
            ?.trim()
            ?.takeIf { it.isNotBlank() }

        btnCrear.isEnabled = false
        btnCrear.text = "Enviando..."

        repository.crearSolicitud(
            deporteId = deporteSeleccionado,
            tipoPerfilId = tipoPerfilSeleccionado,
            institucionId = institucionSeleccionada,
            equipoId = equipoSeleccionado,
            nombreDeportivo = nombreDeportivo,
            fotoDeportiva = null
        ) { correcto, mensaje ->

            if (_binding == null) return@crearSolicitud

            Toast.makeText(
                requireContext(),
                mensaje,
                Toast.LENGTH_LONG
            ).show()

            if (correcto) {
                dialogo.dismiss()
                cargarSolicitudes()
            } else {
                btnCrear.isEnabled = true
                btnCrear.text = "Crear solicitud"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
