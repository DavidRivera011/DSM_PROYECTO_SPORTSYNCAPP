package com.example.sportsyncapp.ui.partidos

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.EquipoPartido
import com.example.sportsyncapp.data.model.EstadoPartido
import com.example.sportsyncapp.data.model.EventoPartido
import com.example.sportsyncapp.data.model.Partido
import com.example.sportsyncapp.data.repository.PartidosRepository
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.FragmentPartidosBinding
import com.example.sportsyncapp.ui.adapters.PartidosAdapter
import com.google.firebase.dataconnect.LocalDate
import java.util.Calendar
import java.util.UUID

class PartidosFragment : Fragment() {

    private var _binding: FragmentPartidosBinding? = null
    private val binding get() = _binding!!

    private val repository = PartidosRepository()

    private lateinit var adapter: PartidosAdapter
    private var esAdministrador = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPartidosBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarLista()

        binding.swipeRefreshMatches.setOnRefreshListener {
            cargarPartidos()
        }

        binding.btnAddMatch.setOnClickListener {
            if (esAdministrador) {
                mostrarFormularioPartido()
            }
        }

        cargarRolUsuario()
    }

    private fun cargarRolUsuario() {
        UsuarioRepository().obtenerUsuarioActual { usuario ->
            if (_binding == null) return@obtenerUsuarioActual

            esAdministrador = usuario?.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            binding.btnAddMatch.visibility =
                if (esAdministrador) View.VISIBLE else View.GONE

            adapter.actualizarPermisos(esAdministrador)
            cargarPartidos()
        }
    }

    private fun configurarLista() {
        adapter = PartidosAdapter(
            partidos = emptyList(),
            esAdministrador = false,
            onEditar = { partido: Partido ->
                if (esAdministrador) {
                    mostrarFormularioPartido(partido)
                }
            },
            onEliminar = { partido: Partido ->
                confirmarEliminar(partido)
            }
        )

        binding.rvMatches.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvMatches.adapter = adapter
    }

    private fun cargarPartidos() {
        binding.swipeRefreshMatches.isRefreshing = true

        repository.obtenerPartidos { resultado ->
            if (_binding == null) return@obtenerPartidos

            binding.swipeRefreshMatches.isRefreshing = false

            if (resultado != null) {
                adapter.actualizarLista(resultado)

                binding.tvMatchesEmpty.visibility =
                    if (resultado.isEmpty()) View.VISIBLE else View.GONE
            } else {
                binding.tvMatchesEmpty.visibility = View.VISIBLE

                Toast.makeText(
                    requireContext(),
                    "No se pudieron cargar los partidos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun mostrarFormularioPartido(partido: Partido? = null) {
        if (!esAdministrador) return

        repository.obtenerEventosPartido { eventos ->
            if (_binding == null) return@obtenerEventosPartido

            if (eventos.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "No hay eventos registrados. Crea un evento primero.",
                    Toast.LENGTH_SHORT
                ).show()
                return@obtenerEventosPartido
            }

            repository.obtenerEquiposPartido { equipos ->
                if (_binding == null) return@obtenerEquiposPartido

                if (equipos.isNullOrEmpty() || equipos.size < 2) {
                    Toast.makeText(
                        requireContext(),
                        "Debes tener al menos 2 equipos registrados.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@obtenerEquiposPartido
                }

                repository.obtenerEstadosPartido { estados ->
                    if (_binding == null) return@obtenerEstadosPartido

                    if (estados.isNullOrEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "No hay estados de partido disponibles.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@obtenerEstadosPartido
                    }

                    mostrarDialogoPartido(partido, eventos, equipos, estados)
                }
            }
        }
    }

    private fun mostrarDialogoPartido(
        partido: Partido?,
        eventos: List<EventoPartido>,
        equipos: List<EquipoPartido>,
        estados: List<EstadoPartido>
    ) {
        if (_binding == null || !esAdministrador) return

        val contexto = requireContext()
        val vista = layoutInflater.inflate(R.layout.dialog_partido, null)

        val tvTitulo = vista.findViewById<TextView>(R.id.tvMatchDialogTitle)
        val spinnerEvento = vista.findViewById<Spinner>(R.id.spinnerEvento)
        val spinnerEquipoLocal = vista.findViewById<Spinner>(R.id.spinnerEquipoLocal)
        val spinnerEquipoVisitante = vista.findViewById<Spinner>(R.id.spinnerEquipoVisitante)
        val btnFecha = vista.findViewById<Button>(R.id.btnFechaPartido)
        val btnHora = vista.findViewById<Button>(R.id.btnHoraPartido)
        val spinnerEstado = vista.findViewById<Spinner>(R.id.spinnerEstadoPartido)

        tvTitulo.text = if (partido == null) "Añadir partido" else "Editar partido"

        val adapterEventos = ArrayAdapter(
            contexto,
            android.R.layout.simple_spinner_item,
            eventos.map { it.nombre }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEvento.adapter = adapterEventos

        val adapterEquipoLocal = ArrayAdapter(
            contexto,
            android.R.layout.simple_spinner_item,
            equipos.map { it.nombreEquipo }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEquipoLocal.adapter = adapterEquipoLocal

        val adapterEquipoVisitante = ArrayAdapter(
            contexto,
            android.R.layout.simple_spinner_item,
            equipos.map { it.nombreEquipo }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEquipoVisitante.adapter = adapterEquipoVisitante

        val adapterEstados = ArrayAdapter(
            contexto,
            android.R.layout.simple_spinner_item,
            estados.map { it.nombreEstado }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEstado.adapter = adapterEstados

        partido?.eventoId?.let { idStr ->
            val idx = eventos.indexOfFirst { it.id.toString() == idStr }
            if (idx >= 0) spinnerEvento.setSelection(idx)
        }

        partido?.equipoLocalId?.let { idStr ->
            val idx = equipos.indexOfFirst { it.id.toString() == idStr }
            if (idx >= 0) spinnerEquipoLocal.setSelection(idx)
        }

        partido?.equipoVisitanteId?.let { idStr ->
            val idx = equipos.indexOfFirst { it.id.toString() == idStr }
            if (idx >= 0) spinnerEquipoVisitante.setSelection(idx)
        } ?: run {
            if (equipos.size > 1) {
                spinnerEquipoVisitante.setSelection(1)
            }
        }

        partido?.estadoId?.let { idStr ->
            val idx = estados.indexOfFirst { it.id.toString() == idStr }
            if (idx >= 0) spinnerEstado.setSelection(idx)
        }

        var fechaYear: Int
        var fechaMonth: Int
        var fechaDay: Int

        val cal = Calendar.getInstance()
        if (!partido?.fecha.isNullOrBlank()) {
            val partes = partido.fecha.split("-")
            fechaYear = partes.getOrNull(0)?.toIntOrNull() ?: cal.get(Calendar.YEAR)
            fechaMonth = partes.getOrNull(1)?.toIntOrNull() ?: (cal.get(Calendar.MONTH) + 1)
            fechaDay = partes.getOrNull(2)?.toIntOrNull() ?: cal.get(Calendar.DAY_OF_MONTH)
        } else {
            fechaYear = cal.get(Calendar.YEAR)
            fechaMonth = cal.get(Calendar.MONTH) + 1
            fechaDay = cal.get(Calendar.DAY_OF_MONTH)
        }

        fun actualizarTextoFecha() {
            btnFecha.text = "Fecha: %04d-%02d-%02d".format(fechaYear, fechaMonth, fechaDay)
        }
        actualizarTextoFecha()

        btnFecha.setOnClickListener {
            val datePicker = DatePickerDialog(
                contexto,
                { _, year, month, dayOfMonth ->
                    fechaYear = year
                    fechaMonth = month + 1
                    fechaDay = dayOfMonth
                    actualizarTextoFecha()
                },
                fechaYear,
                fechaMonth - 1,
                fechaDay
            )
            datePicker.show()
        }

        var horaSeleccionada = partido?.hora?.takeIf { it.isNotBlank() } ?: "12:00"

        fun actualizarTextoHora() {
            btnHora.text = "Hora: $horaSeleccionada"
        }
        actualizarTextoHora()

        btnHora.setOnClickListener {
            val partesHora = horaSeleccionada.split(":")
            val initialHour = partesHora.getOrNull(0)?.toIntOrNull() ?: 12
            val initialMinute = partesHora.getOrNull(1)?.toIntOrNull() ?: 0

            val timePicker = TimePickerDialog(
                contexto,
                { _, hourOfDay, minute ->
                    horaSeleccionada = "%02d:%02d".format(hourOfDay, minute)
                    actualizarTextoHora()
                },
                initialHour,
                initialMinute,
                true
            )
            timePicker.show()
        }

        val dialogo = AlertDialog.Builder(contexto)
            .setView(vista)
            .setPositiveButton(if (partido == null) "Guardar" else "Guardar cambios", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialogo.setOnShowListener {
            val btnGuardar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnCancelar = dialogo.getButton(AlertDialog.BUTTON_NEGATIVE)

            btnGuardar.setTextColor(android.graphics.Color.parseColor("#E85D32"))
            btnCancelar.setTextColor(android.graphics.Color.parseColor("#757575"))

            btnGuardar.setOnClickListener {
                val idxEvento = spinnerEvento.selectedItemPosition
                val idxLocal = spinnerEquipoLocal.selectedItemPosition
                val idxVisitante = spinnerEquipoVisitante.selectedItemPosition
                val idxEstado = spinnerEstado.selectedItemPosition

                if (idxEvento !in eventos.indices ||
                    idxLocal !in equipos.indices ||
                    idxVisitante !in equipos.indices ||
                    idxEstado !in estados.indices
                ) {
                    Toast.makeText(contexto, "Selecciona todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (idxLocal == idxVisitante) {
                    Toast.makeText(
                        contexto,
                        "El equipo local y el visitante no pueden ser el mismo",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val eventoSel = eventos[idxEvento]
                val localSel = equipos[idxLocal]
                val visitanteSel = equipos[idxVisitante]
                val estadoSel = estados[idxEstado]

                val fechaFirebase = LocalDate(
                    fechaYear,
                    fechaMonth,
                    fechaDay
                )

                btnGuardar.isEnabled = false

                val alTerminar: (Boolean) -> Unit = { exito ->
                    if (_binding != null) {
                        if (exito) {
                            Toast.makeText(
                                requireContext(),
                                if (partido == null) "Partido guardado correctamente"
                                else "Partido actualizado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialogo.dismiss()
                            cargarPartidos()
                        } else {
                            btnGuardar.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                "Error al guardar el partido.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                if (partido == null) {
                    repository.crearPartido(
                        eventoId = eventoSel.id,
                        equipoLocalId = localSel.id,
                        equipoVisitanteId = visitanteSel.id,
                        fecha = fechaFirebase,
                        hora = horaSeleccionada,
                        estadoId = estadoSel.id,
                        alTerminar = alTerminar
                    )
                } else {
                    val partidoUUID = try {
                        UUID.fromString(partido.id)
                    } catch (_: Exception) {
                        Toast.makeText(requireContext(), "ID de partido no válido", Toast.LENGTH_SHORT).show()
                        btnGuardar.isEnabled = true
                        return@setOnClickListener
                    }

                    repository.actualizarPartido(
                        id = partidoUUID,
                        eventoId = eventoSel.id,
                        equipoLocalId = localSel.id,
                        equipoVisitanteId = visitanteSel.id,
                        fecha = fechaFirebase,
                        hora = horaSeleccionada,
                        estadoId = estadoSel.id,
                        alTerminar = alTerminar
                    )
                }
            }
        }

        dialogo.show()
    }

    private fun confirmarEliminar(partido: Partido) {
        if (!esAdministrador) return

        val partidoId = try {
            UUID.fromString(partido.id)
        } catch (error: IllegalArgumentException) {
            Toast.makeText(
                requireContext(),
                "El identificador del partido no es válido.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar partido")
            .setMessage(
                "¿Deseas eliminar el partido de ${partido.eventoNombre}?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                repository.eliminarPartido(partidoId) { exito ->
                    if (_binding == null) return@eliminarPartido

                    Toast.makeText(
                        requireContext(),
                        if (exito) "Partido eliminado"
                        else "No se pudo eliminar el partido",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (exito) cargarPartidos()
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
