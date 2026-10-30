package com.example.sportsyncapp.data.model

import com.google.firebase.dataconnect.LocalDate
import java.util.UUID

data class Evento(
    val id: UUID,
    val nombre: String,
    val fecha: LocalDate,
    val deporteId: UUID?,
    val nombreDeporte: String?,
    val institucionId: UUID?,
    val nombreInstitucion: String?,
    val estadoId: UUID,
    val nombreEstado: String?
)

data class DeporteEvento(
    val id: UUID,
    val nombreDeporte: String
)

data class InstitucionEvento(
    val id: UUID,
    val nombre: String
)

data class EstadoEvento(
    val id: UUID,
    val nombreEstado: String
)
