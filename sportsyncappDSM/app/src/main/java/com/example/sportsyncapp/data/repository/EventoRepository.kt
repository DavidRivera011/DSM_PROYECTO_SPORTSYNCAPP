package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.Evento
import com.example.sportsyncapp.data.model.DeporteEvento
import com.example.sportsyncapp.data.model.InstitucionEvento
import com.example.sportsyncapp.data.model.EstadoEvento
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.LocalDate
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class EventoRepository {

    companion object {
        private const val TAG = "EventoRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    // Obtener todos los eventos
    fun obtenerEventos(alTerminar: (List<Evento>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado = conector.getEventos.execute()

                val eventos = resultado.data.eventoss.map { dato ->
                    Evento(
                        id = dato.id,
                        nombre = dato.nombre,
                        fecha = dato.fecha,
                        deporteId = dato.deporte?.id,
                        nombreDeporte = dato.deporte?.nombreDeporte,
                        institucionId = dato.institucion?.id,
                        nombreInstitucion = dato.institucion?.nombre,
                        estadoId = dato.estado.id,
                        nombreEstado = dato.estado.nombreEstado
                    )
                }

                withContext(Dispatchers.Main) {
                    alTerminar(eventos)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener eventos", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // Obtener un evento por su identificador
    fun obtenerEventoPorId(
        id: UUID,
        alTerminar: (Evento?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado =
                    conector.getEventoPorId.execute(id = id)

                val dato = resultado.data.eventoss.firstOrNull()

                val evento = dato?.let {
                    Evento(
                        id = it.id,
                        nombre = it.nombre,
                        fecha = it.fecha,
                        deporteId = it.deporte?.id,
                        nombreDeporte = it.deporte?.nombreDeporte,
                        institucionId = it.institucion?.id,
                        nombreInstitucion = it.institucion?.nombre,
                        estadoId = it.estado.id,
                        nombreEstado = it.estado.nombreEstado
                    )
                }

                withContext(Dispatchers.Main) {
                    alTerminar(evento)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener evento por ID", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // Obtener deportes para el formulario
    fun obtenerDeportesEvento(
        alTerminar: (List<DeporteEvento>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado =
                    conector.getDeportesEvento.execute()

                val deportes = resultado.data.deportess.map {
                    DeporteEvento(
                        id = it.id,
                        nombreDeporte = it.nombreDeporte
                    )
                }

                withContext(Dispatchers.Main) {
                    alTerminar(deportes)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener deportes para eventos", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // Obtener instituciones para el formulario
    fun obtenerInstitucionesEvento(
        alTerminar: (List<InstitucionEvento>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado =
                    conector.getInstitucionesEvento.execute()

                val instituciones =
                    resultado.data.institucioness.map {
                        InstitucionEvento(
                            id = it.id,
                            nombre = it.nombre
                        )
                    }

                withContext(Dispatchers.Main) {
                    alTerminar(instituciones)
                }
            } catch (error: Exception) {
                Log.e(
                    TAG,
                    "Error al obtener instituciones para eventos",
                    error
                )

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // Obtener estados para el formulario
    fun obtenerEstadosEvento(
        alTerminar: (List<EstadoEvento>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado =
                    conector.getEstadosEvento.execute()

                val estados = resultado.data.estadosEventos.mapNotNull {
                    val nombre = it.nombreEstado

                    if (nombre.isNullOrBlank()) {
                        null
                    } else {
                        EstadoEvento(
                            id = it.id,
                            nombreEstado = nombre
                        )
                    }
                }

                withContext(Dispatchers.Main) {
                    alTerminar(estados)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener estados de eventos", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // Crear un evento
    fun crearEvento(
        nombre: String,
        fecha: LocalDate,
        deporteId: UUID,
        institucionId: UUID,
        estadoId: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.createEvento.execute(
                nombre = nombre,
                fecha = fecha,
                deporteId = deporteId,
                institucionId = institucionId,
                estadoId = estadoId
            )
        }
    }

    // Actualizar un evento
    fun actualizarEvento(
        id: UUID,
        nombre: String,
        fecha: LocalDate,
        deporteId: UUID,
        institucionId: UUID,
        estadoId: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.updateEvento.execute(
                id = id,
                nombre = nombre,
                fecha = fecha,
                deporteId = deporteId,
                institucionId = institucionId,
                estadoId = estadoId
            )
        }
    }

    // Eliminar un evento
    fun eliminarEvento(
        id: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.deleteEvento.execute(id = id)
        }
    }

    // Ejecutar operaciones y devolver el resultado en el hilo principal
    private fun ejecutarOperacion(
        alTerminar: (Boolean) -> Unit,
        operacion: suspend () -> Any
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val exito = try {
                operacion()
                true
            } catch (error: Exception) {
                Log.e(TAG, "Error en operación de eventos", error)
                false
            }

            withContext(Dispatchers.Main) {
                alTerminar(exito)
            }
        }
    }
}
