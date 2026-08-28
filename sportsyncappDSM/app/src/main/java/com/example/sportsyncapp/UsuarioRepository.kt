package com.example.sportsyncapp

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.LocalDate
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import com.google.firebase.auth.FirebaseAuth

class UsuarioRepository {

    companion object {
        private const val TAG = "UsuarioRepository"

        // UUID del rol Usuario
        val ROL_USUARIO: UUID =
            UUID.fromString(
                "550e8400-e29b-41d4-a716-446655440001"
            )

        // UUID del rol Administrador
        val ROL_ADMINISTRADOR: UUID =
            UUID.fromString(
                "550e8400-e29b-41d4-a716-446655440002"
            )
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    // ---------------------------------------------------------------
    // GUARDAR USUARIO
    // ---------------------------------------------------------------

    fun guardarUsuarioActual(
        usuario: Usuario,
        alTerminar: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val fechaNacimiento =
                    convertirFecha(usuario.fechaNacimiento)

                if (fechaNacimiento == null) {
                    Log.e(
                        TAG,
                        "Fecha de nacimiento inválida"
                    )

                    withContext(Dispatchers.Main) {
                        alTerminar(false)
                    }

                    return@launch
                }

                conector.createUsuario.execute(
                    nombre = usuario.nombre,
                    apellido = usuario.apellido,
                    correo = usuario.correo,
                    fechaNacimiento = fechaNacimiento,
                    rolId = UUID.fromString(usuario.rolId)
                ) {
                    fotoPerfil = usuario.fotoPerfil
                }

                Log.i(
                    TAG,
                    "Usuario guardado correctamente en SQL Connect"
                )

                // Volvemos al hilo principal antes de tocar la UI
                withContext(Dispatchers.Main) {
                    alTerminar(true)
                }

            } catch (error: Exception) {

                Log.e(
                    TAG,
                    "Error al guardar usuario en SQL Connect",
                    error
                )

                // El callback de la Activity debe ejecutarse en Main
                withContext(Dispatchers.Main) {
                    alTerminar(false)
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // OBTENER USUARIO ACTUAL
    // ---------------------------------------------------------------

    fun obtenerUsuarioActual(
        alTerminar: (Usuario?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val uid = FirebaseAuth.getInstance().currentUser?.uid

                if (uid == null) {
                    Log.w(
                        TAG,
                        "No hay usuario autenticado"
                    )

                    withContext(Dispatchers.Main) {
                        alTerminar(null)
                    }

                    return@launch
                }

                Log.i(
                    TAG,
                    "Buscando usuario en SQL Connect con UID: $uid"
                )

                val resultado =
                    conector.getCurrentUsuario.execute {
                        id = uid
                    }

                val datos =
                    resultado.data?.usuarios

                if (datos == null) {

                    Log.w(
                        TAG,
                        "No existe registro para UID: $uid"
                    )

                    withContext(Dispatchers.Main) {
                        alTerminar(null)
                    }

                    return@launch
                }

                val usuario = Usuario(
                    nombre = datos.nombre,
                    apellido = datos.apellido,
                    correo = datos.correo,
                    fechaNacimiento = datos.fechaNacimiento.toString(),
                    rolId = ROL_USUARIO.toString(),
                    fotoPerfil = datos.fotoPerfil
                )

                Log.i(
                    TAG,
                    "Usuario encontrado: ${usuario.nombre}"
                )

                withContext(Dispatchers.Main) {
                    alTerminar(usuario)
                }

            } catch (error: Exception) {

                Log.e(
                    TAG,
                    "Error al obtener usuario desde SQL Connect",
                    error
                )

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // CONVERTIR FECHA
    // ---------------------------------------------------------------

    private fun convertirFecha(
        fecha: String
    ): LocalDate? {

        return try {

            val partes = fecha.split("/")

            if (partes.size != 3) {
                return null
            }

            val dia = partes[0].toInt()
            val mes = partes[1].toInt()
            val anio = partes[2].toInt()

            LocalDate(
                year = anio,
                month = mes,
                day = dia
            )

        } catch (error: Exception) {

            Log.e(
                TAG,
                "Error convirtiendo fecha: $fecha",
                error
            )

            null
        }
    }
}