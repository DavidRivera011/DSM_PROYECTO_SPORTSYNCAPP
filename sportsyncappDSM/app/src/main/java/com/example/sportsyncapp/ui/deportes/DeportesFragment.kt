
package com.example.sportsyncapp.ui.deportes

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
import com.example.sportsyncapp.data.model.Deporte
import com.example.sportsyncapp.data.repository.DeporteRepository
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.FragmentDeportesBinding
import com.example.sportsyncapp.ui.adapters.DeportesAdapter
import com.google.firebase.auth.FirebaseAuth

class DeportesFragment : Fragment() {

    private var _binding: FragmentDeportesBinding? = null
    private val binding get() = _binding!!

    private val repository = DeporteRepository()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var adapter: DeportesAdapter
    private var deportesList: List<Deporte> = emptyList()
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeportesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecyclerView()

        binding.swipeRefreshSports.setOnRefreshListener {
            cargarDeportes()
        }

        binding.btnAddSport.setOnClickListener {
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
            cargarDeportes()
            return
        }

        UsuarioRepository().obtenerUsuarioActual { usuario ->
            if (_binding == null) return@obtenerUsuarioActual

            if (usuario == null) {
                actualizarInterfazAdmin(false)
                mostrarMensaje("No se pudo obtener tu perfil de usuario")
                cargarDeportes()
                return@obtenerUsuarioActual
            }

            val esAdmin = usuario.rolId ==
                    UsuarioRepository.ROL_ADMINISTRADOR.toString()

            actualizarInterfazAdmin(esAdmin)
            cargarDeportes()
        }
    }

    private fun actualizarInterfazAdmin(esAdmin: Boolean) {
        isAdmin = esAdmin

        _binding?.let { currentBinding ->
            currentBinding.btnAddSport.visibility =
                if (esAdmin) View.VISIBLE else View.GONE

            currentBinding.tvAdminRuleNote.visibility =
                if (esAdmin) View.VISIBLE else View.GONE
        }

        if (::adapter.isInitialized) {
            adapter.updatePermissions(esAdmin)
        }
    }

    private fun configurarRecyclerView() {
        adapter = DeportesAdapter(
            sports = emptyList(),
            isAdmin = isAdmin,
            onSportClick = { deporte ->
                if (isAdmin) {
                    mostrarDialogoEditar(deporte)
                } else {
                    mostrarMensaje(
                        "Solo un administrador puede editar deportes"
                    )
                }
            },
            onDeleteClick = { deporte ->
                if (isAdmin) {
                    confirmarEliminar(deporte)
                } else {
                    mostrarMensaje("No tienes permisos de administrador")
                }
            }
        )

        binding.rvSports.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvSports.adapter = adapter
    }

    private fun cargarDeportes() {
        val currentBinding = _binding ?: return
        currentBinding.swipeRefreshSports.isRefreshing = true

        repository.obtenerDeportes { resultado ->
            val bindingActual = _binding
                ?: return@obtenerDeportes

            bindingActual.swipeRefreshSports.isRefreshing = false

            if (resultado != null) {
                deportesList = resultado
                adapter.updateList(resultado)
            } else {
                mostrarMensaje("No se pudieron cargar los deportes")
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
            hint = "Nombre del deporte"
            setSingleLine(true)
        }

        contenedor.addView(campoNombre)

        val dialogo = AlertDialog.Builder(context)
            .setTitle("Añadir deporte")
            .setView(contenedor)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    val nombre = campoNombre.text.toString().trim()

                    if (nombre.isBlank()) {
                        campoNombre.error =
                            "Escribe el nombre del deporte"
                        return@setOnClickListener
                    }

                    repository.crearDeporte(
                        nombre = nombre
                    ) { exito ->
                        if (_binding == null) {
                            return@crearDeporte
                        }

                        if (exito) {
                            mostrarMensaje(
                                "Deporte creado correctamente"
                            )
                            dialogo.dismiss()
                            cargarDeportes()
                        } else {
                            mostrarMensaje(
                                "No se pudo crear el deporte. " +
                                        "Verifica tus permisos."
                            )
                        }
                    }
                }
        }

        dialogo.show()
    }

    private fun mostrarDialogoEditar(deporte: Deporte) {
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
            hint = "Nombre del deporte"
            setSingleLine(true)
            setText(deporte.nombreDeporte)
        }

        contenedor.addView(campoNombre)

        val dialogo = AlertDialog.Builder(context)
            .setTitle("Editar deporte")
            .setView(contenedor)
            .setPositiveButton("Guardar cambios", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
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
                            "Escribe el nombre del deporte"
                        return@setOnClickListener
                    }

                    repository.actualizarDeporte(
                        id = deporte.id,
                        nombre = nombre
                    ) { exito ->
                        if (_binding == null) {
                            return@actualizarDeporte
                        }

                        if (exito) {
                            mostrarMensaje(
                                "Deporte actualizado correctamente"
                            )
                            dialogo.dismiss()
                            cargarDeportes()
                        } else {
                            mostrarMensaje(
                                "No se pudo actualizar el deporte. " +
                                        "Verifica tus permisos."
                            )
                        }
                    }
                }
        }

        dialogo.show()
    }

    private fun confirmarEliminar(deporte: Deporte) {
        if (!isAdmin) {
            mostrarMensaje("No tienes permisos de administrador")
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar deporte")
            .setMessage(
                "¿Estás seguro de eliminar " +
                        "'${deporte.nombreDeporte}'?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                repository.eliminarDeporte(deporte.id) { exito ->
                    if (_binding == null) {
                        return@eliminarDeporte
                    }

                    if (exito) {
                        mostrarMensaje(
                            "Deporte eliminado correctamente"
                        )
                        cargarDeportes()
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
