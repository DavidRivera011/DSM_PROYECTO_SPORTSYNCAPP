package com.example.sportsyncapp.data.model

import java.util.UUID

data class Inscripcion(
    val id: UUID,
    val eventoId: UUID,
    val eventoNombre: String,
    val fechaEvento: String,
    val equipoId: UUID?,
    val equipoNombre: String?,
    val perfilId: UUID?,
    val nombreDeportivo: String?,
    val solicitanteId: String,
    val nombreSolicitante: String,
    val correoSolicitante: String,
    val fechaInscripcion: String,
    val estadoId: UUID,
    val estadoNombre: String,
    val revisorNombre: String,
    val tipo: String,
    val idEstado: Int
)
