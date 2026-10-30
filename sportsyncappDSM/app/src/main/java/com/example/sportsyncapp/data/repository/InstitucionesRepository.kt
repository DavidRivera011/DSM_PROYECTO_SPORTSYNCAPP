package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.connector.DefaultConnector
import com.example.sportsyncapp.connector.execute
import com.example.sportsyncapp.data.model.Institucion
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class InstitucionesRepository {

    companion object {
        private const val TAG = "InstitucionesRepository"
    }

    private val dataConnect =
        FirebaseDataConnect.getInstance(DefaultConnector.config)

    private val conector =
        DefaultConnector.getInstance(dataConnect)

    // CONSULTAR INSTITUCIONES
    fun obtenerInstituciones(
        alTerminar: (List<Institucion>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resultado = conector.getInstituciones.execute()

                val instituciones = resultado.data.institucioness.map { dato ->
                    Institucion(
                        id = dato.id,
                        nombre = dato.nombre,
                        logo = dato.logo,
                        descripcion = dato.descripcion
                    )
                }

                withContext(Dispatchers.Main) {
                    alTerminar(instituciones)
                }
            } catch (error: Exception) {
                Log.e(TAG, "Error al consultar instituciones", error)

                withContext(Dispatchers.Main) {
                    alTerminar(null)
                }
            }
        }
    }

    // CREAR INSTITUCIÓN
    fun crearInstitucion(
        nombre: String,
        logo: String?,
        descripcion: String?,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.createInstitucion.execute(
                nombre = nombre
            ) {
                this.logo = logo
                this.descripcion = descripcion
            }
        }
    }

    // ACTUALIZAR INSTITUCIÓN
    fun actualizarInstitucion(
        id: UUID,
        nombre: String,
        logo: String?,
        descripcion: String?,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.updateInstitucion.execute(
                id = id,
                nombre = nombre
            ) {
                this.logo = logo
                this.descripcion = descripcion
            }
        }
    }

    // ELIMINAR INSTITUCIÓN
    fun eliminarInstitucion(
        id: UUID,
        alTerminar: (Boolean) -> Unit
    ) {
        ejecutarOperacion(alTerminar) {
            conector.deleteInstitucion.execute(
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
                    "Error al ejecutar operación de instituciones",
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
