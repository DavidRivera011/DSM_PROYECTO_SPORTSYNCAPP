package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.EquipoPartido
import com.example.sportsyncapp.data.model.EstadoPartido
import com.example.sportsyncapp.data.model.EventoPartido
import com.example.sportsyncapp.data.model.Partido
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.LocalDate
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class PartidosRepository {

    companion object {
        private const val TAG = "PartidosRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    // CONSULTAR TODOS LOS PARTIDOS
    fun obtenerPartidos(
        alTerminar: (List<Partido>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado: List<Partido>? = try {
                val respuesta = conector.getPartidos.execute()

                respuesta.data.partidoss.map { dato ->
                    Partido(
                        id = dato.id.toString(),

                        eventoId = dato.evento?.id?.toString(),
                        eventoNombre = dato.evento?.nombre ?: "Sin evento",

                        equipoLocalId = dato.equipoLocal?.id?.toString(),
                        equipoLocalNombre =
                            dato.equipoLocal?.nombreEquipo ?: "Por definir",

                        equipoVisitanteId = dato.equipoVisitante?.id?.toString(),
                        equipoVisitanteNombre =
                            dato.equipoVisitante?.nombreEquipo ?: "Por definir",

                        marcadorLocal = dato.marcadorLocal ?: 0,
                        marcadorVisitante = dato.marcadorVisitante ?: 0,

                        fecha = dato.fecha?.let {
                            "%04d-%02d-%02d".format(
                                it.year,
                                it.month,
                                it.day
                            )
                        } ?: dato.evento?.fecha?.let {
                            "%04d-%02d-%02d".format(
                                it.year,
                                it.month,
                                it.day
                            )
                        } ?: "",

                        hora = dato.hora ?: "",

                        estadoId = dato.estado?.id?.toString(),
                        estadoNombre =
                            dato.estado?.nombreEstado ?: "Pendiente"
                    )
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar partidos", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // OBTENER EVENTOS PARA FORMULARIO DE PARTIDOS
    fun obtenerEventosPartido(
        alTerminar: (List<EventoPartido>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = try {
                val respuesta = conector.getEventosPartido.execute()
                respuesta.data.eventoss.map {
                    EventoPartido(
                        id = it.id,
                        nombre = it.nombre
                    )
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener eventos para partido", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // OBTENER EQUIPOS PARA FORMULARIO DE PARTIDOS
    fun obtenerEquiposPartido(
        alTerminar: (List<EquipoPartido>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = try {
                val respuesta = conector.getEquiposPartido.execute()
                respuesta.data.equiposs.map {
                    EquipoPartido(
                        id = it.id,
                        nombreEquipo = it.nombreEquipo
                    )
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener equipos para partido", error)
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }


    // OBTENER ESTADOS PARA FORMULARIO DE PARTIDOS
    fun obtenerEstadosPartido(
        alTerminar: (List<EstadoPartido>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado: List<EstadoPartido>? = try {
                val respuesta = conector.getEstadosPartido.execute()

                respuesta.data.estadosPartidoss.mapNotNull { estado ->
                    val nombre = estado.nombreEstado

                    if (nombre.isNullOrBlank()) {
                        null
                    } else {
                        EstadoPartido(
                            id = estado.id,
                            nombreEstado = nombre
                        )
                    }
                }
            } catch (error: Exception) {
                Log.e(
                    TAG,
                    "Error al obtener estados para partido",
                    error
                )
                null
            }

            withContext(Dispatchers.Main) {
                alTerminar(resultado)
            }
        }
    }

    // CREAR PARTIDO
    fun crearPartido(
        eventoId: UUID,
        equipoLocalId: UUID,
        equipoVisitanteId: UUID,
        fecha: LocalDate,
        hora: String,
        estadoId: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.createPartido.execute(
                eventoId = eventoId,
                equipoLocalId = equipoLocalId,
                equipoVisitanteId = equipoVisitanteId,
                fecha = fecha,
                hora = hora,
                estadoId = estadoId
            )
        }
    }

    // ACTUALIZAR PARTIDO
    fun actualizarPartido(
        id: UUID,
        eventoId: UUID,
        equipoLocalId: UUID,
        equipoVisitanteId: UUID,
        fecha: LocalDate,
        hora: String,
        estadoId: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.updatePartido.execute(
                id = id,
                eventoId = eventoId,
                equipoLocalId = equipoLocalId,
                equipoVisitanteId = equipoVisitanteId,
                fecha = fecha,
                hora = hora,
                estadoId = estadoId
            )
        }
    }

    // ELIMINAR PARTIDO
    fun eliminarPartido(
        id: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.deletePartido.execute(id = id)
        }
    }

    private fun ejecutarOperacion(
        alTerminar: (Boolean) -> Unit,
        operacion: suspend () -> Any
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val exito = try {
                operacion()
                true
            } catch (error: Exception) {
                Log.e(
                    TAG,
                    "Error al ejecutar operación de partidos",
                    error
                )
                false
            }

            withContext(Dispatchers.Main) {
                alTerminar(exito)
            }
        }
    }
}
