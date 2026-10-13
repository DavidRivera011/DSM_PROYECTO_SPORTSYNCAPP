package com.example.sportsyncapp.data.remote

import android.util.Log
import com.example.sportsyncapp.data.model.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * FirebaseSyncManager â€” Syncs all SportSync data to Firebase Realtime Database.
 *
 * Firebase RTDB structure:
 * sportsync/
 *   deportes/{id}/   â†’ Sport
 *   eventos/{id}/    â†’ SportEvent
 *   inscripciones/{id}/ â†’ Inscription
 *   partidos/{id}/   â†’ Match
 *   usuarios/{uid}/  â†’ User profile
 *
 * Strategy: SQLite = local cache (always works offline)
 *            RTDB   = cloud backup (syncs when connected)
 * On write  â†’ write to SQLite immediately, then async push to RTDB
 * On read   â†’ read from SQLite first; background sync from RTDB on app start
 */
object FirebaseSyncManager {

    private val db by lazy { Firebase.database.reference.child("sportsync") }
    private const val TAG = "FirebaseSyncManager"

    // â”€â”€ Helper: safe fire-and-forget push â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    @OptIn(kotlinx.coroutines.DelicateCoroutinesApi::class)
    private fun safeAsync(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            runCatching { block() }
                .onFailure { Log.w(TAG, "RTDB sync failed: ${it.message}") }
        }
    }

    // â”€â”€ Deportes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    fun pushSport(sport: Sport) = safeAsync {
        db.child("deportes").child(sport.idDeporte.toString()).setValue(
            mapOf(
                "id_deporte" to sport.idDeporte,
                "nombre_deporte" to sport.nombreDeporte,
                "foto_deporte" to sport.fotoDeporte,
                "descripcion" to (sport.descripcion ?: "")
            )
        ).await()
    }

    fun deleteSportRtdb(id: Int) = safeAsync {
        db.child("deportes").child(id.toString()).removeValue().await()
    }

    suspend fun fetchSports(): List<Sport> = runCatching {
        val snapshot = db.child("deportes").get().await()
        snapshot.children.mapNotNull { child ->
            val map = child.value as? Map<*, *> ?: return@mapNotNull null
            Sport(
                idDeporte = (map["id_deporte"] as? Long)?.toInt() ?: 0,
                nombreDeporte = map["nombre_deporte"] as? String ?: "",
                fotoDeporte = map["foto_deporte"] as? String ?: "",
                descripcion = map["descripcion"] as? String
            )
        }
    }.getOrDefault(emptyList())

    // â”€â”€ Eventos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    fun pushEvent(event: SportEvent) = safeAsync {
        db.child("eventos").child(event.idEvento.toString()).setValue(
            mapOf(
                "id_evento" to event.idEvento,
                "id_deporte" to (event.idDeporte ?: 0),
                "deporte_nombre" to event.deporteNombre,
                "id_institucion" to (event.idInstitucion ?: 0),
                "institucion_nombre" to event.institucionNombre,
                "nombre" to event.nombre,
                "fecha" to event.fecha,
                "id_estado" to event.idEstado,
                "estado_nombre" to event.estadoNombre,
                "descripcion" to (event.descripcion ?: "")
            )
        ).await()
    }

    fun deleteEventRtdb(id: Int) = safeAsync {
        db.child("eventos").child(id.toString()).removeValue().await()
    }

    suspend fun fetchEvents(): List<SportEvent> = runCatching {
        val snapshot = db.child("eventos").get().await()
        snapshot.children.mapNotNull { child ->
            val map = child.value as? Map<*, *> ?: return@mapNotNull null
            SportEvent(
                idEvento = (map["id_evento"] as? Long)?.toInt() ?: 0,
                idDeporte = (map["id_deporte"] as? Long)?.toInt(),
                deporteNombre = map["deporte_nombre"] as? String ?: "",
                idInstitucion = (map["id_institucion"] as? Long)?.toInt(),
                institucionNombre = map["institucion_nombre"] as? String ?: "",
                nombre = map["nombre"] as? String ?: "",
                fecha = map["fecha"] as? String ?: "",
                idEstado = (map["id_estado"] as? Long)?.toInt() ?: 1,
                estadoNombre = map["estado_nombre"] as? String ?: "Pendiente",
                descripcion = map["descripcion"] as? String
            )
        }
    }.getOrDefault(emptyList())

    // â”€â”€ Inscripciones â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    fun pushInscription(ins: Inscription) = safeAsync {
        db.child("inscripciones").child(ins.idInscripcion.toString()).setValue(
            mapOf(
                "id_inscripcion" to ins.idInscripcion,
                "id_evento" to ins.idEvento,
                "evento_nombre" to ins.eventoNombre,
                "tipo" to ins.tipo,
                "id_equipo" to (ins.idEquipo ?: 0),
                "equipo_nombre" to (ins.equipoNombre ?: ""),
                "id_perfil" to (ins.idPerfil ?: 0),
                "perfil_nombre" to (ins.perfilNombre ?: ""),
                "id_usuario_solicitante" to ins.idUsuarioSolicitante,
                "nombre_solicitante" to ins.nombreSolicitante,
                "fecha_inscripcion" to ins.fechaInscripcion,
                "id_estado" to ins.idEstado,
                "estado_nombre" to ins.estadoNombre
            )
        ).await()
    }

    fun deleteInscriptionRtdb(id: Int) = safeAsync {
        db.child("inscripciones").child(id.toString()).removeValue().await()
    }

    fun updateInscriptionStatusRtdb(id: Int, idEstado: Int, estadoNombre: String) = safeAsync {
        db.child("inscripciones").child(id.toString()).updateChildren(
            mapOf("id_estado" to idEstado, "estado_nombre" to estadoNombre)
        ).await()
    }

    suspend fun fetchInscriptions(): List<Inscription> = runCatching {
        val snapshot = db.child("inscripciones").get().await()
        snapshot.children.mapNotNull { child ->
            val map = child.value as? Map<*, *> ?: return@mapNotNull null
            Inscription(
                idInscripcion = (map["id_inscripcion"] as? Long)?.toInt() ?: 0,
                idEvento = (map["id_evento"] as? Long)?.toInt() ?: 0,
                eventoNombre = map["evento_nombre"] as? String ?: "",
                tipo = map["tipo"] as? String ?: "Equipo",
                idEquipo = (map["id_equipo"] as? Long)?.toInt()?.takeIf { it != 0 },
                equipoNombre = (map["equipo_nombre"] as? String)?.ifEmpty { null },
                idPerfil = (map["id_perfil"] as? Long)?.toInt()?.takeIf { it != 0 },
                perfilNombre = (map["perfil_nombre"] as? String)?.ifEmpty { null },
                idUsuarioSolicitante = (map["id_usuario_solicitante"] as? Long)?.toInt() ?: 0,
                nombreSolicitante = map["nombre_solicitante"] as? String ?: "",
                fechaInscripcion = map["fecha_inscripcion"] as? String ?: "",
                idEstado = (map["id_estado"] as? Long)?.toInt() ?: 1,
                estadoNombre = map["estado_nombre"] as? String ?: "Pendiente"
            )
        }
    }.getOrDefault(emptyList())

    // â”€â”€ Partidos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    fun pushMatch(match: Match) = safeAsync {
        db.child("partidos").child(match.idPartido.toString()).setValue(
            mapOf(
                "id_partido" to match.idPartido,
                "id_evento" to (match.idEvento ?: 0),
                "evento_nombre" to (match.eventoNombre ?: ""),
                "id_equipo_local" to match.idEquipoLocal,
                "equipo_local_nombre" to match.equipoLocalNombre,
                "id_equipo_visitante" to match.idEquipoVisitante,
                "equipo_visitante_nombre" to match.equipoVisitanteNombre,
                "marcador_local" to match.marcadorLocal,
                "marcador_visitante" to match.marcadorVisitante,
                "fecha" to match.fecha,
                "hora" to match.hora,
                "id_estado" to match.idEstado,
                "estado_nombre" to match.estadoNombre
            )
        ).await()
    }

    fun updateMatchScoreRtdb(id: Int, local: Int, visitante: Int) = safeAsync {
        db.child("partidos").child(id.toString()).updateChildren(
            mapOf("marcador_local" to local, "marcador_visitante" to visitante)
        ).await()
    }

    suspend fun fetchMatches(): List<Match> = runCatching {
        val snapshot = db.child("partidos").get().await()
        snapshot.children.mapNotNull { child ->
            val map = child.value as? Map<*, *> ?: return@mapNotNull null
            Match(
                idPartido = (map["id_partido"] as? Long)?.toInt() ?: 0,
                idEvento = (map["id_evento"] as? Long)?.toInt(),
                eventoNombre = map["evento_nombre"] as? String,
                idEquipoLocal = (map["id_equipo_local"] as? Long)?.toInt() ?: 0,
                equipoLocalNombre = map["equipo_local_nombre"] as? String ?: "",
                idEquipoVisitante = (map["id_equipo_visitante"] as? Long)?.toInt() ?: 0,
                equipoVisitanteNombre = map["equipo_visitante_nombre"] as? String ?: "",
                marcadorLocal = (map["marcador_local"] as? Long)?.toInt() ?: 0,
                marcadorVisitante = (map["marcador_visitante"] as? Long)?.toInt() ?: 0,
                fecha = map["fecha"] as? String ?: "",
                hora = map["hora"] as? String ?: "",
                idEstado = (map["id_estado"] as? Long)?.toInt() ?: 1,
                estadoNombre = map["estado_nombre"] as? String ?: "Programado"
            )
        }
    }.getOrDefault(emptyList())

    // â”€â”€ Usuario/Perfil â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    fun pushUser(uid: String, user: com.example.sportsyncapp.data.model.User) = safeAsync {
        db.child("usuarios").child(uid).setValue(
            mapOf(
                "id_usuario" to user.idUsuario,
                "nombre" to user.nombre,
                "apellido" to user.apellido,
                "correo" to user.correo,
                "fecha_nacimiento" to user.fechaNacimiento,
                "id_rol" to user.idRol,
                "rol_nombre" to user.rolNombre
            )
        ).await()
    }

    suspend fun fetchUser(uid: String): com.example.sportsyncapp.data.model.User? = runCatching {
        val snapshot = db.child("usuarios").child(uid).get().await()
        val map = snapshot.value as? Map<*, *> ?: return@runCatching null
        com.example.sportsyncapp.data.model.User(
            idUsuario = (map["id_usuario"] as? Long)?.toInt() ?: 0,
            nombre = map["nombre"] as? String ?: "",
            apellido = map["apellido"] as? String ?: "",
            correo = map["correo"] as? String ?: "",
            fechaNacimiento = map["fecha_nacimiento"] as? String ?: "",
            idRol = (map["id_rol"] as? Long)?.toInt() ?: 1,
            rolNombre = map["rol_nombre"] as? String ?: "FAN"
        )
    }.getOrNull()
}

