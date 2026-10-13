package com.example.sportsyncapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id_usuario") val idUsuario: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("apellido") val apellido: String = "",
    @SerializedName("correo") val correo: String = "",
    @SerializedName("contrasena") val contrasena: String = "",
    @SerializedName("foto_perfil") val fotoPerfil: String? = null,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String = "",
    @SerializedName("id_rol") val idRol: Int = 1, // 1 = FAN, 2 = ADMIN
    @SerializedName("rol_nombre") val rolNombre: String = if (idRol == 2) "ADMINISTRADOR" else "FAN"
) : Serializable

data class Sport(
    @SerializedName("id_deporte") val idDeporte: Int = 0,
    @SerializedName("nombre_deporte") val nombreDeporte: String = "",
    @SerializedName("foto_deporte") val fotoDeporte: String = "",
    @SerializedName("descripcion") val descripcion: String? = null
) : Serializable

data class Institution(
    @SerializedName("id_institucion") val idInstitucion: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("logo") val logo: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("deportes") val deportes: List<String> = emptyList()
) : Serializable

data class Team(
    @SerializedName("id_equipo") val idEquipo: Int = 0,
    @SerializedName("id_institucion") val idInstitucion: Int? = null,
    @SerializedName("institucion_nombre") val institucionNombre: String? = null,
    @SerializedName("id_deporte") val idDeporte: Int? = null,
    @SerializedName("deporte_nombre") val deporteNombre: String? = null,
    @SerializedName("nombre_equipo") val nombreEquipo: String = ""
) : Serializable

data class SportEvent(
    @SerializedName("id_evento") val idEvento: Int = 0,
    @SerializedName("id_deporte") val idDeporte: Int? = null,
    @SerializedName("deporte_nombre") val deporteNombre: String = "",
    @SerializedName("id_institucion") val idInstitucion: Int? = null,
    @SerializedName("institucion_nombre") val institucionNombre: String = "",
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("fecha") val fecha: String = "",
    @SerializedName("id_estado") val idEstado: Int = 1, // 1: Pendiente, 2: En curso, 3: Finalizado
    @SerializedName("estado_nombre") val estadoNombre: String = "Pendiente",
    @SerializedName("descripcion") val descripcion: String? = null
) : Serializable

data class Match(
    @SerializedName("id_partido") val idPartido: Int = 0,
    @SerializedName("id_evento") val idEvento: Int? = null,
    @SerializedName("evento_nombre") val eventoNombre: String? = null,
    @SerializedName("id_equipo_local") val idEquipoLocal: Int = 0,
    @SerializedName("equipo_local_nombre") val equipoLocalNombre: String = "",
    @SerializedName("id_equipo_visitante") val idEquipoVisitante: Int = 0,
    @SerializedName("equipo_visitante_nombre") val equipoVisitanteNombre: String = "",
    @SerializedName("marcador_local") var marcadorLocal: Int = 0,
    @SerializedName("marcador_visitante") var marcadorVisitante: Int = 0,
    @SerializedName("fecha") val fecha: String = "",
    @SerializedName("hora") val hora: String = "",
    @SerializedName("id_estado") val idEstado: Int = 1, // 1: Programado, 2: En curso, 3: Finalizado
    @SerializedName("estado_nombre") val estadoNombre: String = "Programado"
) : Serializable

data class MatchParticipant(
    @SerializedName("id_participante") val idParticipante: Int = 0,
    @SerializedName("id_partido") val idPartido: Int = 0,
    @SerializedName("id_perfil") val idPerfil: Int = 0,
    @SerializedName("nombre_jugador") val nombreJugador: String = "",
    @SerializedName("equipo_nombre") val equipoNombre: String = "",
    @SerializedName("titular") val titular: Boolean = true,
    @SerializedName("posicion") val posicion: String? = null
) : Serializable

data class AthleteProfile(
    @SerializedName("id_perfil") val idPerfil: Int = 0,
    @SerializedName("id_usuario") val idUsuario: Int = 0,
    @SerializedName("nombre_completo") val nombreCompleto: String = "",
    @SerializedName("nombre_deportivo") val nombreDeportivo: String = "",
    @SerializedName("institucion_nombre") val institucionNombre: String = "",
    @SerializedName("equipo_nombre") val equipoNombre: String = "",
    @SerializedName("deporte_nombre") val deporteNombre: String = "",
    @SerializedName("tipo_perfil_nombre") val tipoPerfilNombre: String = "Jugador",
    @SerializedName("puntos_totales") val puntosTotales: Int = 0,
    @SerializedName("partidos_jugados") val partidosJugados: Int = 0,
    @SerializedName("foto_deportiva") val fotoDeportiva: String? = null
) : Serializable

data class Inscription(
    @SerializedName("id_inscripcion") val idInscripcion: Int = 0,
    @SerializedName("id_evento") val idEvento: Int = 0,
    @SerializedName("evento_nombre") val eventoNombre: String = "",
    @SerializedName("tipo") val tipo: String = "Equipo", // "Equipo" o "Individual"
    @SerializedName("id_equipo") val idEquipo: Int? = null,
    @SerializedName("equipo_nombre") val equipoNombre: String? = null,
    @SerializedName("id_perfil") val idPerfil: Int? = null,
    @SerializedName("perfil_nombre") val perfilNombre: String? = null,
    @SerializedName("id_usuario_solicitante") val idUsuarioSolicitante: Int = 0,
    @SerializedName("nombre_solicitante") val nombreSolicitante: String = "",
    @SerializedName("fecha_inscripcion") val fechaInscripcion: String = "",
    @SerializedName("id_estado") var idEstado: Int = 1, // 1: Pendiente, 2: Aprobado, 3: Rechazado
    @SerializedName("estado_nombre") var estadoNombre: String = "Pendiente"
) : Serializable

data class RankingItem(
    @SerializedName("posicion") val posicion: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("equipo_o_institucion") val equipoOInstitucion: String = "",
    @SerializedName("deporte") val deporte: String = "",
    @SerializedName("puntos") val puntos: Int = 0
) : Serializable

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null
)
