package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.Equipo
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class EquipoRepository {

    companion object {
        private const val TAG = "EquipoRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    fun obtenerEquipos(alTerminar: (List<Equipo>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado = conector.getEquipos.execute()
                val equipos = resultado.data.equiposs.map { dato ->
                    Equipo(
                        id = dato.id,
                        nombreEquipo = dato.nombreEquipo,
                        institucionId = dato.institucion?.id,
                        nombreInstitucion = dato.institucion?.nombre,
                        deporteId = dato.deporte?.id,
                        nombreDeporte = dato.deporte?.nombreDeporte
                    )
                }

                withContext(Dispatchers.Main) {
                    alTerminar(equipos)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al obtener equipos", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    fun crearEquipo(
        nombre: String,
        institucionId: UUID,
        deporteId: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.createEquipo.execute(
                nombreEquipo = nombre,
                institucionId = institucionId,
                deporteId = deporteId
            )
        }
    }

    fun actualizarEquipo(
        id: UUID,
        nombre: String,
        institucionId: UUID,
        deporteId: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.updateEquipo.execute(
                id = id,
                nombreEquipo = nombre,
                institucionId = institucionId,
                deporteId = deporteId
            )
        }
    }

    fun eliminarEquipo(
        id: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.deleteEquipo.execute(id = id)
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
                Log.e(TAG, "Error en operación de equipos", error)
                false
            }

            withContext(Dispatchers.Main) {
                alTerminar(exito)
            }
        }
    }
}
