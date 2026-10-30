package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.Inscripcion
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InscripcionRepository {

    companion object {
        private const val TAG = "InscripcionRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    fun obtenerInscripciones(
        alTerminar: (List<Inscripcion>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            val resultado: List<Inscripcion>? = try {

                val respuesta =
                    conector.getInscripcionesAdmin.execute()

                respuesta.data.inscripcioness.map { dato ->


                    Inscripcion(
                        id = dato.id,
                        eventoId = dato.evento.id,
                        eventoNombre = dato.evento.nombre,
                        fechaEvento = dato.evento.fecha.toString(),

                        equipoId = dato.equipo?.id,
                        equipoNombre = dato.equipo?.nombreEquipo,

                        perfilId = dato.perfil?.id,
                        nombreDeportivo = dato.perfil?.nombreDeportivo,

                        solicitanteId = dato.usuarioSolicitante.id,
                        nombreSolicitante =
                            "${dato.usuarioSolicitante.nombre} ${dato.usuarioSolicitante.apellido}",
                        correoSolicitante = dato.usuarioSolicitante.correo,

                        fechaInscripcion = dato.fechaInscripcion.toString(),

                        estadoId = dato.estado.id,
                        estadoNombre = dato.estado.nombreEstado ?: "Sin estado",

                        revisorNombre = dato.usuarioRevisor?.let {
                            "${it.nombre} ${it.apellido}"
                        } ?: "Sin revisor",

                        tipo = if (dato.equipo != null) "Equipo" else "Individual",

                        idEstado = when (
                            dato.estado.nombreEstado?.trim()?.lowercase()
                        ) {
                            "pendiente" -> 1
                            "aprobado", "aprobada", "aceptado", "aceptada" -> 2
                            "rechazado", "rechazada" -> 3
                            else -> 0
                        }
                    )

                }

            } catch (error: Exception) {
                Log.e(
                    TAG,
                    "Error al consultar inscripciones",
                    error
                )
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }
}
