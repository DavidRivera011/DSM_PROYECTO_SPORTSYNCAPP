package com.example.sportsyncapp.data.remote

import com.example.sportsyncapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface SportSyncApiService {

    // Deportes
    @GET("api/deportes")
    suspend fun getDeportes(): Response<List<Sport>>

    @POST("api/deportes")
    suspend fun createDeporte(@Body deporte: Sport): Response<Sport>

    @DELETE("api/deportes/{id}")
    suspend fun deleteDeporte(@Path("id") id: Int): Response<ApiResponse<Boolean>>

    // Instituciones
    @GET("api/instituciones")
    suspend fun getInstituciones(): Response<List<Institution>>

    @POST("api/instituciones")
    suspend fun createInstitucion(@Body institucion: Institution): Response<Institution>

    // Equipos
    @GET("api/equipos")
    suspend fun getEquipos(): Response<List<Team>>

    // Eventos / Torneos
    @GET("api/eventos")
    suspend fun getEventos(): Response<List<SportEvent>>

    @POST("api/eventos")
    suspend fun createEvento(@Body evento: SportEvent): Response<SportEvent>

    // Partidos
    @GET("api/partidos")
    suspend fun getPartidos(): Response<List<Match>>

    @PUT("api/partidos/{id}/marcador")
    suspend fun updateMarcador(
        @Path("id") id: Int,
        @Query("local") local: Int,
        @Query("visitante") visitante: Int
    ): Response<Match>

    // Alineaciones
    @GET("api/partidos/{id}/participantes")
    suspend fun getParticipantesPartido(@Path("id") matchId: Int): Response<List<MatchParticipant>>

    // Inscripciones
    @GET("api/inscripciones")
    suspend fun getInscripciones(): Response<List<Inscription>>

    @POST("api/inscripciones")
    suspend fun createInscripcion(@Body inscripcion: Inscription): Response<Inscription>

    @PUT("api/inscripciones/{id}/estado")
    suspend fun updateEstadoInscripcion(
        @Path("id") id: Int,
        @Query("id_estado") idEstado: Int
    ): Response<Inscription>

    @DELETE("api/inscripciones/{id}")
    suspend fun cancelInscripcion(@Path("id") id: Int): Response<ApiResponse<Boolean>>

    // Perfiles & Ranking
    @GET("api/perfiles/{userId}")
    suspend fun getPerfilDeportivo(@Path("userId") userId: Int): Response<AthleteProfile>

    @GET("api/ranking")
    suspend fun getRanking(): Response<List<RankingItem>>
}

