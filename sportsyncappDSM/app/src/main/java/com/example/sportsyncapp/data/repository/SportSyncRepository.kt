package com.example.sportsyncapp.data.repository

import android.content.Context
import com.example.sportsyncapp.data.local.PreferencesManager
import com.example.sportsyncapp.data.local.SportSyncDbHelper
import com.example.sportsyncapp.data.model.*
import com.example.sportsyncapp.data.remote.ApiClient
import com.example.sportsyncapp.data.remote.FirebaseSyncManager
import com.example.sportsyncapp.data.remote.SportSyncApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * SportSyncRepository — Single source of truth.
 *
 * Read priority:  REST API → Firebase RTDB → SQLite (offline-first fallback)
 * Write priority: SQLite immediately (instant UI) → RTDB async (cloud backup) → REST API try
 */
class SportSyncRepository(private val context: Context) {

    private val api: SportSyncApiService = ApiClient.apiService
    private val dbHelper = SportSyncDbHelper(context)
    val preferences = PreferencesManager(context)

    // ====================================================================
    // Deportes
    // ====================================================================

    suspend fun getSports(): Result<List<Sport>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDeportes()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        // Try Firebase RTDB
        val rtdb = FirebaseSyncManager.fetchSports()
        if (rtdb.isNotEmpty()) return@withContext Result.success(rtdb)
        // Fallback to SQLite
        Result.success(dbHelper.getAllSports())
    }

    suspend fun createSport(sport: Sport): Result<Sport> = withContext(Dispatchers.IO) {
        // 1. Save to SQLite immediately
        val id = dbHelper.insertSport(sport)
        val saved = sport.copy(idDeporte = id.toInt())
        // 2. Sync to Firebase RTDB (async)
        FirebaseSyncManager.pushSport(saved)
        // 3. Try REST API
        try {
            val response = api.createDeporte(saved)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.success(saved)
    }

    suspend fun deleteSport(id: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        dbHelper.deleteSport(id)
        FirebaseSyncManager.deleteSportRtdb(id)
        try { api.deleteDeporte(id) } catch (_: Exception) {}
        Result.success(true)
    }

    // ====================================================================
    // Instituciones
    // ====================================================================

    suspend fun getInstitutions(): Result<List<Institution>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getInstituciones()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.failure(Exception("Error al cargar instituciones"))
    }

    // ====================================================================
    // Equipos
    // ====================================================================

    suspend fun getTeams(): Result<List<Team>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getEquipos()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.failure(Exception("Error al cargar equipos"))
    }

    // ====================================================================
    // Eventos — SQLite + RTDB + REST
    // ====================================================================

    suspend fun getEvents(): Result<List<SportEvent>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getEventos()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        val rtdb = FirebaseSyncManager.fetchEvents()
        if (rtdb.isNotEmpty()) return@withContext Result.success(rtdb)
        Result.success(dbHelper.getAllEvents())
    }

    suspend fun createEvent(event: SportEvent): Result<SportEvent> = withContext(Dispatchers.IO) {
        val id = dbHelper.insertEvent(event)
        val saved = event.copy(idEvento = id.toInt())
        FirebaseSyncManager.pushEvent(saved)
        try {
            val response = api.createEvento(saved)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.success(saved)
    }

    suspend fun deleteEvent(id: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        dbHelper.deleteEvent(id)
        FirebaseSyncManager.deleteEventRtdb(id)
        Result.success(true)
    }

    // ====================================================================
    // Partidos — SQLite + RTDB + REST
    // ====================================================================

    suspend fun getMatches(): Result<List<Match>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPartidos()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        val rtdb = FirebaseSyncManager.fetchMatches()
        if (rtdb.isNotEmpty()) return@withContext Result.success(rtdb)
        Result.success(dbHelper.getAllMatches())
    }

    suspend fun updateMatchScore(id: Int, local: Int, visitante: Int): Result<Match> = withContext(Dispatchers.IO) {
        dbHelper.updateMatchScore(id, local, visitante)
        FirebaseSyncManager.updateMatchScoreRtdb(id, local, visitante)
        try {
            val response = api.updateMarcador(id, local, visitante)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        val updated = dbHelper.getAllMatches().find { it.idPartido == id }
        if (updated != null) Result.success(updated)
        else Result.failure(Exception("Partido no encontrado"))
    }

    // ====================================================================
    // Alineaciones / Participantes
    // ====================================================================

    suspend fun getMatchParticipants(matchId: Int): Result<List<MatchParticipant>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getParticipantesPartido(matchId)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.failure(Exception("Error al cargar participantes"))
    }

    // ====================================================================
    // Inscripciones — SQLite + RTDB + REST
    // ====================================================================

    suspend fun getInscriptions(): Result<List<Inscription>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getInscripciones()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        val rtdb = FirebaseSyncManager.fetchInscriptions()
        if (rtdb.isNotEmpty()) return@withContext Result.success(rtdb)
        Result.success(dbHelper.getAllInscriptions())
    }

    suspend fun createInscription(inscription: Inscription): Result<Inscription> = withContext(Dispatchers.IO) {
        val id = dbHelper.insertInscription(inscription)
        val saved = inscription.copy(idInscripcion = id.toInt())
        FirebaseSyncManager.pushInscription(saved)
        try {
            val response = api.createInscripcion(saved)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.success(saved)
    }

    suspend fun updateInscriptionStatus(id: Int, statusId: Int): Result<Inscription> = withContext(Dispatchers.IO) {
        val estadoNombre = when (statusId) {
            2 -> "Aprobado"
            3 -> "Rechazado"
            else -> "Pendiente"
        }
        dbHelper.updateInscriptionStatus(id, statusId, estadoNombre)
        FirebaseSyncManager.updateInscriptionStatusRtdb(id, statusId, estadoNombre)
        try {
            val response = api.updateEstadoInscripcion(id, statusId)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        val updated = dbHelper.getAllInscriptions().find { it.idInscripcion == id }
        if (updated != null) Result.success(updated)
        else Result.failure(Exception("Inscripción no encontrada"))
    }

    suspend fun cancelInscription(id: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        dbHelper.deleteInscription(id)
        FirebaseSyncManager.deleteInscriptionRtdb(id)
        try { api.cancelInscripcion(id) } catch (_: Exception) {}
        Result.success(true)
    }

    // ====================================================================
    // Perfil y Ranking
    // ====================================================================

    suspend fun getAthleteProfile(userId: Int = 1): Result<AthleteProfile> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPerfilDeportivo(userId)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.failure(Exception("Error al cargar perfil"))
    }

    suspend fun getRanking(): Result<List<RankingItem>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getRanking()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.success(response.body()!!)
            }
        } catch (_: Exception) {}
        Result.failure(Exception("Error al cargar ranking"))
    }

    // ====================================================================
    // Sync user to Firebase RTDB on login/register
    // ====================================================================

    fun syncCurrentUserToFirebase() {
        val fbUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser ?: return
        val user = preferences.currentUser ?: return
        FirebaseSyncManager.pushUser(fbUser.uid, user)
    }
}
