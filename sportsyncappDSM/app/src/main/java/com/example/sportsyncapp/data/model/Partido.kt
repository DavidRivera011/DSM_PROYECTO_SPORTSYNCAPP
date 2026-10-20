package com.example.sportsyncapp.data.model

import java.io.Serializable
import java.util.UUID

data class Partido(
    // Identificador del partido
    val id: String = "",

    // Información del evento
    val eventoId: String? = null,
    val eventoNombre: String = "",

    // Información de los equipos
    val equipoLocalId: String? = null,
    val equipoLocalNombre: String = "",
    val equipoVisitanteId: String? = null,
    val equipoVisitanteNombre: String = "",

    // Marcador
    val marcadorLocal: Int = 0,
    val marcadorVisitante: Int = 0,

    // Fecha y hora
    val fecha: String = "",
    val hora: String = "",

    // Estado del partido
    val estadoId: String? = null,
    val estadoNombre: String = "Pendiente"
) : Serializable

data class EventoPartido(
    val id: UUID,
    val nombre: String
)

data class EquipoPartido(
    val id: UUID,
    val nombreEquipo: String
)

data class EstadoPartido(
    val id: UUID,
    val nombreEstado: String
)
