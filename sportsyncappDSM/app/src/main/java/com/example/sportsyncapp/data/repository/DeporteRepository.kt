
package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.Deporte
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class DeporteRepository {

    companion object {
        private const val TAG = "DeporteRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    // CONSULTAR DEPORTES
    fun obtenerDeportes(
        alTerminar: (List<Deporte>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Iniciando consulta GetDeportes")

                val resultado = conector.getDeportes.execute()
                val datos = resultado.data.deportess

                Log.d(
                    TAG,
                    "GetDeportes devolvió ${datos.size} deportes"
                )

                val deportes = datos.map { dato ->
                    Deporte(
                        id = dato.id,
                        nombreDeporte = dato.nombreDeporte
                    )
                }

                deportes.forEach { deporte ->
                    Log.d(
                        TAG,
                        "Deporte: ${deporte.nombreDeporte}, ID: ${deporte.id}"
                    )
                }

                withContext(Dispatchers.Main) {
                    alTerminar(deportes)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar deportes", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // CREAR DEPORTE
    fun crearDeporte(
        nombre: String,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.createDeporte.execute(
                nombreDeporte = nombre
            )
        }
    }

    // ACTUALIZAR DEPORTE
    fun actualizarDeporte(
        id: UUID,
        nombre: String,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.updateDeporte.execute(
                id = id,
                nombreDeporte = nombre
            )
        }
    }

    // ELIMINAR DEPORTE
    fun eliminarDeporte(
        id: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.deleteDeporte.execute(
                id = id
            )
        }
    }

    // EJECUTAR OPERACIONES DE ESCRITURA
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
                    "Error al ejecutar operación de deportes",
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
