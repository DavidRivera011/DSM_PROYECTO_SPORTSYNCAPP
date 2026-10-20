package com.example.sportsyncapp.data.model

import java.util.UUID

data class Equipo(
    val id: UUID,
    val nombreEquipo: String,
    val institucionId: UUID?,
    val nombreInstitucion: String?,
    val deporteId: UUID?,
    val nombreDeporte: String?
)
