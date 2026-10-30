package com.example.sportsyncapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.Solicitud
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.LocalDate
import com.google.firebase.dataconnect.getInstance
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SolicitudRepository {

    companion object {
        private const val TAG = "SolicitudRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    // Consulta exclusivamente las solicitudes del usuario autenticado.
    fun obtenerSolicitudes(
        alTerminar: (List<Solicitud>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado: List<Solicitud>? = try {
                val respuesta =
                    conector.getSolicitudesPropias.execute()

                respuesta.data.solicitudess.map { dato ->
                    Solicitud(
                        id = dato.id,
                        usuarioId = "",
                        nombreUsuario = "",
                        apellidoUsuario = "",
                        correoUsuario = "",

                        deporteId = dato.deporte.id,
                        nombreDeporte = dato.deporte.nombreDeporte,

                        tipoPerfilId = dato.tipoPerfil.id,
                        nombrePerfil = dato.tipoPerfil.nombrePerfil,

                        institucionId = dato.institucion?.id,
                        nombreInstitucion = dato.institucion?.nombre,

                        equipoId = dato.equipo?.id,
                        nombreEquipo = dato.equipo?.nombreEquipo,

                        nombreDeportivo = dato.nombreDeportivo,
                        fotoDeportiva = dato.fotoDeportiva,

                        fechaSolicitud = dato.fechaSolicitud.toString(),

                        estadoId = dato.estado.id,
                        estadoNombre = dato.estado.nombreEstado,

                        revisorNombre = "Pendiente de revisión"
                    )
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar mis solicitudes", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // Consulta todas las solicitudes para el administrador.
    fun obtenerSolicitudesAdmin(
        alTerminar: (List<Solicitud>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado: List<Solicitud>? = try {
                val respuesta =
                    conector.getSolicitudesAdmin.execute()

                respuesta.data.solicitudess.map { dato ->
                    Solicitud(
                        id = dato.id,

                        usuarioId = dato.usuario.id,
                        nombreUsuario = dato.usuario.nombre,
                        apellidoUsuario = dato.usuario.apellido,
                        correoUsuario = dato.usuario.correo,

                        deporteId = dato.deporte.id,
                        nombreDeporte = dato.deporte.nombreDeporte,

                        tipoPerfilId = dato.tipoPerfil.id,
                        nombrePerfil = dato.tipoPerfil.nombrePerfil,

                        institucionId = dato.institucion?.id,
                        nombreInstitucion = dato.institucion?.nombre,

                        equipoId = dato.equipo?.id,
                        nombreEquipo = dato.equipo?.nombreEquipo,

                        nombreDeportivo = dato.nombreDeportivo,
                        fotoDeportiva = dato.fotoDeportiva,

                        fechaSolicitud = dato.fechaSolicitud.toString(),

                        estadoId = dato.estado.id,
                        estadoNombre = dato.estado.nombreEstado,

                        revisorNombre = "Revisión administrativa"
                    )
                }
            } catch (error: Exception) {
                Log.e(
                    TAG,
                    "Error al consultar todas las solicitudes",
                    error
                )
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // Aprobar o rechazar una solicitud.
    // Solo el administrador puede ejecutar esta operación.
    fun actualizarSolicitud(
        solicitudId: UUID,
        estadoId: UUID,
        alTerminar: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var correcto = false
            var mensaje = "No se pudo actualizar la solicitud."

            try {
                conector.updateSolicitud.execute(
                    id = solicitudId,
                    estadoId = estadoId
                )

                correcto = true
                mensaje = "Solicitud actualizada correctamente."

            } catch (error: Exception) {
                Log.e(TAG, "Error al actualizar solicitud", error)
                mensaje = error.localizedMessage
                    ?: "Ocurrió un error al actualizar la solicitud."
            }

            withContext(Dispatchers.Main) {
                alTerminar(correcto, mensaje)
            }
        }
    }

    // Obtener los deportes disponibles para el formulario.
    fun obtenerDeportes(
        alTerminar: (List<Pair<UUID, String>>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = try {
                conector.getDeportes.execute()
                    .data.deportess.map {
                        it.id to it.nombreDeporte
                    }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar deportes", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // Obtener las instituciones disponibles.
    fun obtenerInstituciones(
        alTerminar: (List<Pair<UUID, String>>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = try {
                conector.getInstituciones.execute()
                    .data.institucioness.map {
                        it.id to it.nombre
                    }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar instituciones", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // Obtener los equipos disponibles.
    fun obtenerEquipos(
        alTerminar: (List<Pair<UUID, String>>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = try {
                conector.getEquipos.execute()
                    .data.equiposs.map {
                        it.id to it.nombreEquipo
                    }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar equipos", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // Obtener los tipos de perfil disponibles.
    fun obtenerTiposPerfil(
        alTerminar: (List<Pair<UUID, String>>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = try {
                conector.getTiposPerfil.execute()
                    .data.tipoPerfils.map { tipo ->
                        tipo.id to tipo.nombrePerfil
                    }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar tipos de perfil", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // Crear una solicitud de perfil deportivo.
    @RequiresApi(Build.VERSION_CODES.O)
    fun crearSolicitud(
        deporteId: UUID,
        tipoPerfilId: UUID,
        institucionId: UUID?,
        equipoId: UUID?,
        nombreDeportivo: String?,
        fotoDeportiva: String?,
        alTerminar: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var mensaje = "No se pudo crear la solicitud."
            var correcto = false
            val hoy = java.time.LocalDate.now()

            try {
                conector.createSolicitud.execute(
                    deporteId = deporteId,
                    tipoPerfilId = tipoPerfilId,
                    fechaSolicitud = LocalDate(
                        hoy.year,
                        hoy.monthValue,
                        hoy.dayOfMonth
                    )
                ) {
                    this.institucionId = institucionId
                    this.equipoId = equipoId
                    this.nombreDeportivo = nombreDeportivo
                        ?.takeIf { it.isNotBlank() }
                    this.fotoDeportiva = fotoDeportiva
                        ?.takeIf { it.isNotBlank() }
                }

                correcto = true
                mensaje = "Tu solicitud se creó correctamente."
            } catch (error: Exception) {
                Log.e(TAG, "Error al crear solicitud", error)
                mensaje = error.localizedMessage
                    ?: "Ocurrió un error al enviar la solicitud."
            }

            withContext(Dispatchers.Main) {
                alTerminar(correcto, mensaje)
            }
        }
    }
}
