package com.example.sportsyncapp.data.repository

import android.util.Log
import com.example.sportsyncapp.data.model.HomeResumen

class HomeRepository {

    companion object {
        private const val TAG = "HomeRepository"
    }
    private val usuarioRepository = UsuarioRepository()
    private val deporteRepository = DeporteRepository()

    fun obtenerResumen(
        alTerminar: (HomeResumen?) -> Unit
    ) {
        usuarioRepository.obtenerUsuarioActual { usuario ->

            if (usuario == null) {
                Log.w(TAG, "No se pudo obtener el usuario actual")
                alTerminar(null)
                return@obtenerUsuarioActual
            }

            deporteRepository.obtenerDeportes { deportes ->

                val totalDeportes = deportes?.size ?: 0
                if (deportes == null) {
                    Log.e(TAG, "No se pudieron consultar los deportes")
                }

                val esAdministrador =
                    usuario.rolId ==
                            UsuarioRepository.ROL_ADMINISTRADOR.toString()

                Log.d(TAG, "Total de deportes: $totalDeportes")
                Log.d(TAG, "¿Es administrador?: $esAdministrador")

                alTerminar(
                    HomeResumen(
                        nombreUsuario = usuario.nombre,
                        totalDeportes = totalDeportes,
                        esAdministrador = esAdministrador
                    )
                )
            }
        }
    }
}
