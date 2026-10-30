package com.example.sportsyncapp.ui.equipos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sportsyncapp.data.model.Equipo
import com.example.sportsyncapp.data.model.Deporte
import com.example.sportsyncapp.data.repository.DeporteRepository
import com.example.sportsyncapp.data.repository.EquipoRepository
import com.example.sportsyncapp.data.repository.InstitucionesRepository
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.data.model.Institucion
import com.example.sportsyncapp.databinding.FragmentEquiposBinding
import com.example.sportsyncapp.ui.adapters.EquiposAdapter
import com.google.firebase.auth.FirebaseAuth
import com.example.sportsyncapp.R
import android.text.Editable
import android.text.TextWatcher


class EquiposFragment : Fragment() {

    private var _binding: FragmentEquiposBinding? = null
    private val binding get() = _binding!!

    private val equipoRepository = EquipoRepository()
    private val deporteRepository = DeporteRepository()
    private val institucionRepository = InstitucionesRepository()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var adapter: EquiposAdapter
    private var isAdmin = false

    private var deportes: List<Deporte> = emptyList()
    private var instituciones: List<Institucion> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquiposBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecyclerView()

        binding.etSearchTeams.addTextChangedListener(object : TextWatcher {

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
                adapter.filtrarEquipos(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        binding.swipeRefreshTeams.setOnRefreshListener {
            cargarEquipos()
        }

        binding.btnAddTeam.setOnClickListener {
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
            mostrarMensaje("Debes iniciar sesión para consultar los equipos")
            cargarEquipos()
            return
        }

        UsuarioRepository().obtenerUsuarioActual { usuario ->
            if (_binding == null) return@obtenerUsuarioActual

            val esAdmin = usuario?.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            actualizarInterfazAdmin(esAdmin)
            cargarEquipos()
        }
    }

    private fun actualizarInterfazAdmin(esAdmin: Boolean) {
        isAdmin = esAdmin

        _binding?.let {
            it.btnAddTeam.visibility =
                if (esAdmin) View.VISIBLE else View.GONE

            it.tvTeamAdminNote.visibility =
                if (esAdmin) View.VISIBLE else View.GONE
        }

        if (::adapter.isInitialized) {
            adapter.actualizarPermisos(esAdmin)
        }
    }

    private fun configurarRecyclerView() {
        adapter = EquiposAdapter(
            equipos = emptyList(),
            esAdministrador = isAdmin,
            onEditar = { equipo ->
                if (isAdmin) {
                    cargarCatalogosYMostrarDialogo(equipo)
                }
            },
            onEliminar = { equipo ->
                if (isAdmin) {
                    confirmarEliminar(equipo)
                }
            }
        )

        binding.rvTeams.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )

        binding.rvTeams.adapter = adapter
    }

    private fun cargarEquipos() {
        val currentBinding = _binding ?: return
        currentBinding.swipeRefreshTeams.isRefreshing = true

        equipoRepository.obtenerEquipos { resultado ->
            val bindingActual = _binding ?: return@obtenerEquipos
            bindingActual.swipeRefreshTeams.isRefreshing = false

            if (resultado != null) {
                adapter.actualizarLista(resultado)
            } else {
                mostrarMensaje("No se pudieron cargar los equipos")
            }
        }
    }

    private fun cargarCatalogosYMostrarDialogo(equipo: Equipo?) {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        deporteRepository.obtenerDeportes { listaDeportes ->
            if (_binding == null) return@obtenerDeportes

            if (listaDeportes == null) {
                mostrarMensaje("No se pudieron cargar los deportes")
                return@obtenerDeportes
            }

            deportes = listaDeportes

            institucionRepository.obtenerInstituciones { listaInstituciones ->
                if (_binding == null) return@obtenerInstituciones

                if (listaInstituciones == null) {
                    mostrarMensaje("No se pudieron cargar las instituciones")
                    return@obtenerInstituciones
                }

                instituciones = listaInstituciones

                if (deportes.isEmpty() || instituciones.isEmpty()) {
                    mostrarMensaje(
                        "Primero debes registrar al menos un deporte y una institución"
                    )
                    return@obtenerInstituciones
                }

                mostrarDialogoEquipo(equipo)
            }
        }
    }


    private fun mostrarDialogoEquipo(equipo: Equipo?) {
        if (!isAdmin || _binding == null) return

        val context = requireContext()

        // Cargar el diseño personalizado
        val vista = layoutInflater.inflate(
            R.layout.dialog_equipo,
            null
        )

        val tvTitulo = vista.findViewById<android.widget.TextView>(
            R.id.tvDialogTitle
        )

        val campoNombre = vista.findViewById<EditText>(
            R.id.etTeamName
        )

        val spinnerInstitucion = vista.findViewById<Spinner>(
            R.id.spTeamInstitution
        )

        val spinnerDeporte = vista.findViewById<Spinner>(
            R.id.spTeamSport
        )

        // Configurar el título según la operación
        tvTitulo.text = if (equipo == null) {
            "Añadir equipo"
        } else {
            "Editar equipo"
        }

        campoNombre.setText(equipo?.nombreEquipo.orEmpty())

        // Adaptador de instituciones
        val adaptadorInstituciones = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            instituciones.map { it.nombre }
        ).apply {
            setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        // Adaptador de deportes
        val adaptadorDeportes = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            deportes.map { it.nombreDeporte }
        ).apply {
            setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        spinnerInstitucion.adapter = adaptadorInstituciones
        spinnerDeporte.adapter = adaptadorDeportes

        // Seleccionar los valores existentes al editar
        equipo?.institucionId?.let { id ->
            val indice = instituciones.indexOfFirst { it.id == id }

            if (indice >= 0) {
                spinnerInstitucion.setSelection(indice)
            }
        }

        equipo?.deporteId?.let { id ->
            val indice = deportes.indexOfFirst { it.id == id }

            if (indice >= 0) {
                spinnerDeporte.setSelection(indice)
            }
        }

        val dialogo = AlertDialog.Builder(context)
            .setView(vista)
            .setPositiveButton(
                if (equipo == null) "Guardar equipo"
                else "Guardar cambios",
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
                    mostrarMensaje(
                        "No tienes permisos de administrador"
                    )
                    dialogo.dismiss()
                    return@setOnClickListener
                }

                val nombre = campoNombre.text.toString().trim()

                if (nombre.isBlank()) {
                    campoNombre.error =
                        "Escribe el nombre del equipo"
                    campoNombre.requestFocus()
                    return@setOnClickListener
                }

                val indiceInstitucion =
                    spinnerInstitucion.selectedItemPosition

                val indiceDeporte =
                    spinnerDeporte.selectedItemPosition

                if (
                    indiceInstitucion !in instituciones.indices ||
                    indiceDeporte !in deportes.indices
                ) {
                    mostrarMensaje(
                        "Selecciona una institución y un deporte"
                    )
                    return@setOnClickListener
                }

                val institucion = instituciones[indiceInstitucion]
                val deporte = deportes[indiceDeporte]

                botonGuardar.isEnabled = false

                val terminar: (Boolean) -> Unit = terminar@{ exito ->
                    if (_binding == null) return@terminar

                    if (exito) {
                        mostrarMensaje(
                            if (equipo == null) {
                                "Equipo creado correctamente"
                            } else {
                                "Equipo actualizado correctamente"
                            }
                        )

                        dialogo.dismiss()
                        cargarEquipos()
                    } else {
                        botonGuardar.isEnabled = true

                        mostrarMensaje(
                            "No se pudo guardar el equipo. " +
                                    "Verifica los permisos y las relaciones."
                        )
                    }
                }

                if (equipo == null) {
                    equipoRepository.crearEquipo(
                        nombre,
                        institucion.id,
                        deporte.id,
                        terminar
                    )
                } else {
                    equipoRepository.actualizarEquipo(
                        equipo.id,
                        nombre,
                        institucion.id,
                        deporte.id,
                        terminar
                    )
                }
            }
        }

        dialogo.show()
    }


    private fun confirmarEliminar(equipo: Equipo) {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar equipo")
            .setMessage(
                "¿Estás seguro de eliminar '${equipo.nombreEquipo}'?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                equipoRepository.eliminarEquipo(equipo.id) { exito ->
                    if (_binding == null) return@eliminarEquipo

                    if (exito) {
                        mostrarMensaje("Equipo eliminado correctamente")
                        cargarEquipos()
                    } else {
                        mostrarMensaje(
                            "No se pudo eliminar. Puede que existan registros asociados o falten permisos."
                        )
                    }
                }
            }
            .show()
    }

    private fun mostrarMensaje(mensaje: String) {
        context?.let {
            Toast.makeText(it, mensaje, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
