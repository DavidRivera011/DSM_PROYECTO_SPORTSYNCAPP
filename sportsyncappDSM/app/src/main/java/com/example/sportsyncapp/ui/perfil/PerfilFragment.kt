
package com.example.sportsyncapp.ui.perfil

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sportsyncapp.LoginActivity
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.FragmentPerfilBinding
import com.example.sportsyncapp.ui.main.MainActivity
import com.example.sportsyncapp.ui.solicitudes.SolicitudesFragment
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val usuarioRepository = UsuarioRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        // Abrir la pantalla de edición
        binding.btnEditarPerfil.setOnClickListener {
            val intent = Intent(
                requireContext(),
                EditarPerfilActivity::class.java
            )
            startActivity(intent)
        }

        // Abrir mis solicitudes
        binding.btnSolicitudes.setOnClickListener {
            (activity as? MainActivity)?.abrirPantallaSecundaria(
                SolicitudesFragment(),
                "solicitudes"
            )
        }

        cargarPerfil()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        // Actualizar los datos al volver de EditarPerfilActivity
        if (_binding != null) {
            cargarPerfil()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarPerfil() {
        val currentBinding = _binding ?: return

        currentBinding.tvNombre.text = "Cargando perfil..."
        currentBinding.tvNombreCabecera.text = "Cargando perfil..."

        usuarioRepository.obtenerUsuarioActual { usuario ->
            val bindingActual = _binding ?: return@obtenerUsuarioActual

            if (usuario == null) {
                bindingActual.tvNombre.text =
                    "No se pudo cargar el perfil"
                bindingActual.tvNombreCabecera.text =
                    "No se pudo cargar el perfil"
                return@obtenerUsuarioActual
            }

            // Identificar el rol
            val rol = when (usuario.rolId) {
                UsuarioRepository.ROL_ADMINISTRADOR.toString() ->
                    "Administrador"

                UsuarioRepository.ROL_USUARIO.toString() ->
                    "Usuario"

                else -> "FAN"
            }

            // Mostrar Mis solicitudes únicamente a usuarios normales
            bindingActual.btnSolicitudes.visibility =
                if (rol == "Administrador") {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            // Nombre completo
            val nombreCompleto =
                "${usuario.nombre} ${usuario.apellido}"

            bindingActual.tvNombre.text = nombreCompleto
            bindingActual.tvNombreCabecera.text = nombreCompleto

            // Correo y rol
            bindingActual.tvCorreo.text = usuario.correo
            bindingActual.tvRol.text = rol
            bindingActual.tvRolCabecera.text = rol

            // Fecha de nacimiento
            bindingActual.tvFechaNacimiento.text =
                formatearFecha(usuario.fechaNacimiento)

            // Foto de perfil
            if (!usuario.fotoPerfil.isNullOrBlank()) {
                Glide.with(this)
                    .load(usuario.fotoPerfil)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .circleCrop()
                    .into(bindingActual.ivFotoPerfil)
            } else {
                bindingActual.ivFotoPerfil.setImageResource(
                    R.drawable.ic_profile
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatearFecha(fechaTexto: String): String {
        return try {
            val texto = fechaTexto.trim()

            val fecha = if (texto.startsWith("LocalDate(")) {
                val patron = Regex(
                    """LocalDate\(year=(\d+),\s*month=(\d+),\s*day=(\d+)\)"""
                )

                val coincidencia = patron.find(texto)
                    ?: throw IllegalArgumentException(
                        "Formato de fecha inválido"
                    )

                LocalDate.of(
                    coincidencia.groupValues[1].toInt(),
                    coincidencia.groupValues[2].toInt(),
                    coincidencia.groupValues[3].toInt()
                )
            } else {
                LocalDate.parse(texto)
            }

            fecha.format(
                DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )
            )
        } catch (_: Exception) {
            "Fecha no disponible"
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(
            requireContext(),
            LoginActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
