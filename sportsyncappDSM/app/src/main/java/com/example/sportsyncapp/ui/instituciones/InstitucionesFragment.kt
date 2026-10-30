package com.example.sportsyncapp.ui.instituciones

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.data.model.Institucion
import com.example.sportsyncapp.data.repository.InstitucionesRepository
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.FragmentInstitucionesBinding
import com.example.sportsyncapp.ui.adapters.InstitucionesAdapter
import com.google.firebase.auth.FirebaseAuth

class InstitucionesFragment : Fragment() {

    private var _binding: FragmentInstitucionesBinding? = null
    private val binding get() = _binding!!

    private val repository = InstitucionesRepository()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var adapter: InstitucionesAdapter
    private var institucionesList: List<Institucion> = emptyList()

    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstitucionesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecyclerView()

        binding.swipeRefreshInstituciones.setOnRefreshListener {
            cargarInstituciones()
        }

        binding.btnAddInstitution.setOnClickListener {
            if (isAdmin) {
                mostrarDialogoAgregar()
            } else {
                mostrarMensaje("No tienes permisos de administrador")
            }
        }

        cargarRolUsuario()
    }

    private fun cargarRolUsuario() {
        if (firebaseAuth.currentUser == null) {
            actualizarInterfazAdmin(false)
            mostrarMensaje("Debes iniciar sesión para consultar tu perfil")
            cargarInstituciones()
            return
        }

        UsuarioRepository().obtenerUsuarioActual { usuario ->
            if (_binding == null) return@obtenerUsuarioActual

            if (usuario == null) {
                actualizarInterfazAdmin(false)
                mostrarMensaje("No se pudo obtener tu perfil de usuario")
                cargarInstituciones()
                return@obtenerUsuarioActual
            }

            val esAdmin = usuario.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            actualizarInterfazAdmin(esAdmin)
            cargarInstituciones()
        }
    }

    private fun actualizarInterfazAdmin(esAdmin: Boolean) {
        isAdmin = esAdmin

        _binding?.btnAddInstitution?.visibility =
            if (esAdmin) View.VISIBLE else View.GONE

        if (::adapter.isInitialized) {
            adapter.updatePermissions(esAdmin)
        }
    }

    private fun configurarRecyclerView() {
        adapter = InstitucionesAdapter(
            instituciones = emptyList(),
            isAdmin = isAdmin,
            onInstitutionClick = { institucion ->
                mostrarDialogoDetalles(institucion)
            },
            onDeleteClick = { institucion ->
                if (isAdmin) {
                    confirmarEliminar(institucion)
                } else {
                    mostrarMensaje("No tienes permisos de administrador")
                }
            }
        )

        binding.rvInstituciones.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvInstituciones.adapter = adapter
    }

    private fun cargarInstituciones() {
        val currentBinding = _binding ?: return
        currentBinding.swipeRefreshInstituciones.isRefreshing = true

        repository.obtenerInstituciones { resultado ->
            val bindingActual = _binding ?: return@obtenerInstituciones

            bindingActual.swipeRefreshInstituciones.isRefreshing = false

            if (resultado != null) {
                institucionesList = resultado
                adapter.updateList(resultado)

                if (resultado.isEmpty()) {
                    mostrarMensaje("Todavía no hay instituciones registradas")
                }
            } else {
                mostrarMensaje("No se pudieron cargar las instituciones")
            }
        }
    }

    private fun mostrarDialogoAgregar() {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        val context = requireContext()

        val contenedor = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val campoNombre = EditText(context).apply {
            hint = "Nombre de la institución"
            setSingleLine(true)
        }

        val campoLogo = EditText(context).apply {
            hint = "URL del logo (opcional)"
            setSingleLine(true)
        }

        val campoDescripcion = EditText(context).apply {
            hint = "Descripción de la institución"
            minLines = 3
            maxLines = 5
            setSingleLine(false)
            gravity = android.view.Gravity.TOP
        }

        contenedor.addView(campoNombre)
        contenedor.addView(campoLogo)
        contenedor.addView(campoDescripcion)

        val dialogo = AlertDialog.Builder(context)
            .setTitle("Añadir institución")
            .setView(contenedor)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    val nombre = campoNombre.text.toString().trim()
                    val logoIngresado = campoLogo.text.toString().trim()
                    val logo = logoIngresado.ifBlank { null }

                    val descripcionIngresada =
                        campoDescripcion.text.toString().trim()
                    val descripcion =
                        descripcionIngresada.ifBlank { null }

                    if (nombre.isBlank()) {
                        campoNombre.error =
                            "Escribe el nombre de la institución"
                        return@setOnClickListener
                    }

                    repository.crearInstitucion(
                        nombre = nombre,
                        logo = logo,
                        descripcion = descripcion
                    ) { exito ->
                        if (_binding == null) {
                            return@crearInstitucion
                        }

                        if (exito) {
                            mostrarMensaje(
                                "Institución creada correctamente"
                            )
                            dialogo.dismiss()
                            cargarInstituciones()
                        } else {
                            mostrarMensaje(
                                "No se pudo crear la institución. " +
                                        "Verifica tus permisos."
                            )
                        }
                    }
                }
        }

        dialogo.show()
    }

    private fun mostrarDialogoDetalles(institucion: Institucion) {
        val mensaje = buildString {
            append(institucion.nombre)
            append("\n\n")
            append(
                institucion.descripcion?.takeIf { it.isNotBlank() }
                    ?: "Sin descripción registrada"
            )
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Información de la institución")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun confirmarEliminar(institucion: Institucion) {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar institución")
            .setMessage(
                "¿Estás seguro de eliminar '${institucion.nombre}'?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                repository.eliminarInstitucion(institucion.id) { exito ->
                    if (_binding == null) {
                        return@eliminarInstitucion
                    }

                    if (exito) {
                        mostrarMensaje(
                            "Institución eliminada correctamente"
                        )
                        cargarInstituciones()
                    } else {
                        mostrarMensaje(
                            "No se pudo eliminar. Puede que existan " +
                                    "registros asociados o falten permisos."
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
