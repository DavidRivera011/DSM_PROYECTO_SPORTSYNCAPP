package com.example.sportsyncapp.ui.eventos

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sportsyncapp.data.model.Evento
import com.example.sportsyncapp.data.repository.EventoRepository
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.FragmentEventosBinding
import com.example.sportsyncapp.ui.adapters.EventosAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dataconnect.LocalDate
import java.time.LocalDate as JavaLocalDate
import com.example.sportsyncapp.R

class EventosFragment : Fragment() {

    private var _binding: FragmentEventosBinding? = null
    private val binding get() = _binding!!

    private val eventoRepository = EventoRepository()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var adapter: EventosAdapter

    private var isAdmin = false
    private var fechaSeleccionada: JavaLocalDate = JavaLocalDate.now()

    private var buscando = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventosBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecyclerView()
        configurarBuscador()

        binding.swipeRefreshEvents.setOnRefreshListener {
            cargarEventos()
        }

        binding.btnAddEvent.setOnClickListener {
            if (isAdmin) {
                cargarCatalogosYMostrarDialogo(null)
            } else {
                mostrarMensaje("No tienes permisos de administrador")
            }
        }

        cargarRolUsuario()
    }

    private fun cargarRolUsuario() {
        if (firebaseAuth.currentUser == null) {
            actualizarInterfazAdmin(false)
            mostrarMensaje("Debes iniciar sesión para consultar los eventos")
            cargarEventos()
            return
        }

        UsuarioRepository().obtenerUsuarioActual { usuario ->
            if (_binding == null) return@obtenerUsuarioActual

            val esAdmin = usuario?.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            actualizarInterfazAdmin(esAdmin)
            cargarEventos()
        }
    }

    private fun actualizarInterfazAdmin(esAdmin: Boolean) {
        isAdmin = esAdmin

        _binding?.let {
            it.btnAddEvent.visibility =
                if (esAdmin) View.VISIBLE else View.GONE

            it.tvEventAdminNote.visibility =
                if (esAdmin) View.VISIBLE else View.GONE
        }

        if (::adapter.isInitialized) {
            adapter.actualizarPermisos(esAdmin)
        }
    }

    private fun configurarRecyclerView() {
        adapter = EventosAdapter(
            eventos = emptyList(),
            esAdministrador = isAdmin,
            onEditar = { evento ->
                if (isAdmin) {
                    cargarCatalogosYMostrarDialogo(evento)
                }
            },
            onEliminar = { evento ->
                if (isAdmin) {
                    confirmarEliminar(evento)
                }
            }
        )

        binding.rvEvents.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )

        binding.rvEvents.adapter = adapter
    }

    private fun configurarBuscador() {
        binding.etSearchEvents.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    adapter.filtrarEventos(s?.toString().orEmpty())
                    actualizarMensajeVacio()
                }

                override fun afterTextChanged(s: Editable?) {}
            }
        )
    }

    private fun cargarEventos() {
        val bindingActual = _binding ?: return

        bindingActual.swipeRefreshEvents.isRefreshing = true

        eventoRepository.obtenerEventos { resultado ->
            val vistaActual = _binding ?: return@obtenerEventos

            vistaActual.swipeRefreshEvents.isRefreshing = false

            if (resultado != null) {
                adapter.actualizarLista(resultado)
                actualizarMensajeVacio()
            } else {
                mostrarMensaje("No se pudieron cargar los eventos")
            }
        }
    }

    private fun actualizarMensajeVacio() {
        val vistaActual = _binding ?: return

        val sinResultados = adapter.itemCount == 0

        vistaActual.tvEventsEmpty.visibility =
            if (sinResultados) View.VISIBLE else View.GONE

        vistaActual.tvEventsEmpty.text =
            if (vistaActual.etSearchEvents.text.toString().isBlank()) {
                "No hay eventos registrados."
            } else {
                "No hay eventos que coincidan con la búsqueda."
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarCatalogosYMostrarDialogo(evento: Evento?) {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        eventoRepository.obtenerDeportesEvento { deportes ->
            if (_binding == null) return@obtenerDeportesEvento

            if (deportes == null || deportes.isEmpty()) {
                mostrarMensaje("No hay deportes registrados para los eventos")
                return@obtenerDeportesEvento
            }

            eventoRepository.obtenerInstitucionesEvento { instituciones ->
                if (_binding == null) {
                    return@obtenerInstitucionesEvento
                }

                if (instituciones == null || instituciones.isEmpty()) {
                    mostrarMensaje("No hay instituciones registradas")
                    return@obtenerInstitucionesEvento
                }


                eventoRepository.obtenerEstadosEvento { estados ->
                    if (_binding == null) {
                        return@obtenerEstadosEvento
                    }

                    if (estados == null || estados.isEmpty()) {
                        mostrarMensaje("No hay estados de evento registrados")
                        return@obtenerEstadosEvento
                    }

                    val estadosDisponibles = if (evento == null) {
                        estados.filter {
                            it.nombreEstado.equals("Próximo", ignoreCase = true) ||
                                    it.nombreEstado.equals(
                                        "Inscripciones abiertas",
                                        ignoreCase = true
                                    )
                        }
                    } else {
                        estados
                    }

                    if (estadosDisponibles.isEmpty()) {
                        mostrarMensaje(
                            "No se encontraron los estados iniciales permitidos. " +
                                    "Verifica los nombres registrados en Firebase."
                        )
                        return@obtenerEstadosEvento
                    }

                    mostrarDialogoEvento(
                        evento,
                        deportes,
                        instituciones,
                        estadosDisponibles
                    )
                }

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDialogoEvento(
        evento: Evento?,
        deportes: List<com.example.sportsyncapp.data.model.DeporteEvento>,
        instituciones: List<com.example.sportsyncapp.data.model.InstitucionEvento>,
        estados: List<com.example.sportsyncapp.data.model.EstadoEvento>
    ) {
        if (!isAdmin || _binding == null) return

        val contexto = requireContext()

        val vista = layoutInflater.inflate(
            R.layout.dialog_evento,
            null
        )

        val tvTitulo = vista.findViewById<TextView>(
            R.id.tvEventDialogTitle
        )

        val campoNombre = vista.findViewById<EditText>(
            R.id.etEventName
        )

        val botonFecha = vista.findViewById<Button>(
            R.id.btnEventDate
        )

        val spinnerDeporte = vista.findViewById<Spinner>(
            R.id.spEventSport
        )

        val spinnerInstitucion = vista.findViewById<Spinner>(
            R.id.spEventInstitution
        )

        val spinnerEstado = vista.findViewById<Spinner>(
            R.id.spEventStatus
        )

        tvTitulo.text = if (evento == null) {
            "Añadir evento"
        } else {
            "Editar evento"
        }

        campoNombre.setText(evento?.nombre.orEmpty())

        fun <T> configurarSpinner(
            spinner: Spinner,
            elementos: List<T>,
            obtenerNombre: (T) -> String
        ) {
            val adaptador = ArrayAdapter(
                contexto,
                android.R.layout.simple_spinner_item,
                elementos.map(obtenerNombre)
            )

            adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            spinner.adapter = adaptador
        }

        configurarSpinner(
            spinnerDeporte,
            deportes
        ) { it.nombreDeporte }

        configurarSpinner(
            spinnerInstitucion,
            instituciones
        ) { it.nombre }

        configurarSpinner(
            spinnerEstado,
            estados
        ) { it.nombreEstado }

        evento?.deporteId?.let { id ->
            val indice = deportes.indexOfFirst { it.id == id }
            if (indice >= 0) spinnerDeporte.setSelection(indice)
        }

        evento?.institucionId?.let { id ->
            val indice = instituciones.indexOfFirst { it.id == id }
            if (indice >= 0) spinnerInstitucion.setSelection(indice)
        }

        val indiceEstado = estados.indexOfFirst {
            it.id == evento?.estadoId
        }

        if (indiceEstado >= 0) {
            spinnerEstado.setSelection(indiceEstado)
        }

        fechaSeleccionada = try {
            evento?.fecha?.toString()?.let {
                java.time.LocalDate.parse(it)
            } ?: java.time.LocalDate.now()
        } catch (_: Exception) {
            java.time.LocalDate.now()
        }

        fun actualizarFecha() {
            botonFecha.text = "Fecha: $fechaSeleccionada"
        }

        actualizarFecha()

        botonFecha.setOnClickListener {
            val fecha = fechaSeleccionada

            val selectorFecha = DatePickerDialog(
                contexto,
                { _, anio, mes, dia ->
                    val fechaElegida = java.time.LocalDate.of(
                        anio,
                        mes + 1,
                        dia
                    )

                    if (fechaElegida.isBefore(java.time.LocalDate.now())) {
                        mostrarMensaje("No puedes seleccionar una fecha anterior a hoy")
                    } else {
                        fechaSeleccionada = fechaElegida
                        actualizarFecha()
                    }
                },
                fecha.year,
                fecha.monthValue - 1,
                fecha.dayOfMonth
            )

            // Impedir seleccionar fechas anteriores al día actual.
            selectorFecha.datePicker.minDate =
                java.util.Calendar.getInstance().apply {
                    set(
                        java.util.Calendar.HOUR_OF_DAY,
                        0
                    )
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                    set(java.util.Calendar.MILLISECOND, 0)
                }.timeInMillis

            selectorFecha.show()
        }

        val dialogo = AlertDialog.Builder(contexto)
            .setView(vista)
            .setPositiveButton(
                if (evento == null) "Guardar" else "Guardar cambios",
                null
            )
            .setNegativeButton("Cancelar", null)
            .create()

        dialogo.setOnShowListener {
            val botonGuardar = dialogo.getButton(
                AlertDialog.BUTTON_POSITIVE
            )

            val botonCancelar = dialogo.getButton(
                AlertDialog.BUTTON_NEGATIVE
            )

            botonGuardar.setTextColor(
                android.graphics.Color.parseColor("#E85D32")
            )

            botonCancelar.setTextColor(
                android.graphics.Color.parseColor("#757575")
            )

            botonGuardar.setOnClickListener {
                if (!isAdmin) {
                    mostrarMensaje("No tienes permisos de administrador")
                    dialogo.dismiss()
                    return@setOnClickListener
                }

                val nombre = campoNombre.text.toString().trim()

                if (nombre.isBlank()) {
                    campoNombre.error = "Escribe el nombre del evento"
                    campoNombre.requestFocus()
                    return@setOnClickListener
                }

                if (
                    evento == null &&
                    fechaSeleccionada.isBefore(java.time.LocalDate.now())
                ) {
                    mostrarMensaje("La fecha del evento no puede ser anterior a hoy")
                    return@setOnClickListener
                }

                val indiceDeporte = spinnerDeporte.selectedItemPosition
                val indiceInstitucion =
                    spinnerInstitucion.selectedItemPosition
                val indiceEstadoSeleccionado =
                    spinnerEstado.selectedItemPosition

                if (
                    indiceDeporte !in deportes.indices ||
                    indiceInstitucion !in instituciones.indices ||
                    indiceEstadoSeleccionado !in estados.indices
                ) {
                    mostrarMensaje("Selecciona todos los campos")
                    return@setOnClickListener
                }

                val fechaFirebase = com.google.firebase.dataconnect.LocalDate(
                    fechaSeleccionada.year,
                    fechaSeleccionada.monthValue,
                    fechaSeleccionada.dayOfMonth
                )

                val deporte = deportes[indiceDeporte]
                val institucion = instituciones[indiceInstitucion]
                val estado = estados[indiceEstadoSeleccionado]

                botonGuardar.isEnabled = false

                val terminar: (Boolean) -> Unit = terminar@{ exito ->
                    if (_binding == null) return@terminar

                    if (exito) {
                        mostrarMensaje(
                            if (evento == null) {
                                "Evento creado correctamente"
                            } else {
                                "Evento actualizado correctamente"
                            }
                        )

                        dialogo.dismiss()
                        cargarEventos()
                    } else {
                        botonGuardar.isEnabled = true

                        mostrarMensaje(
                            "No se pudo guardar el evento. " +
                                    "Verifica los permisos y los datos."
                        )
                    }
                }

                if (evento == null) {
                    eventoRepository.crearEvento(
                        nombre,
                        fechaFirebase,
                        deporte.id,
                        institucion.id,
                        estado.id,
                        terminar
                    )
                } else {
                    eventoRepository.actualizarEvento(
                        evento.id,
                        nombre,
                        fechaFirebase,
                        deporte.id,
                        institucion.id,
                        estado.id,
                        terminar
                    )
                }
            }
        }

        dialogo.show()
    }


    private fun confirmarEliminar(evento: Evento) {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar evento")
            .setMessage(
                "¿Estás seguro de eliminar '${evento.nombre}'?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                if (!isAdmin) {
                    mostrarMensaje("No tienes permisos de administrador")
                    return@setPositiveButton
                }

                eventoRepository.eliminarEvento(evento.id) { exito ->
                    if (_binding == null) return@eliminarEvento

                    if (exito) {
                        mostrarMensaje("Evento eliminado correctamente")
                        cargarEventos()
                    } else {
                        mostrarMensaje(
                            "No se pudo eliminar el evento. " +
                                    "Puede tener registros asociados."
                        )
                    }
                }
            }
            .show()
    }

    private fun mostrarMensaje(mensaje: String) {
        context?.let {
            Toast.makeText(
                it,
                mensaje,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
