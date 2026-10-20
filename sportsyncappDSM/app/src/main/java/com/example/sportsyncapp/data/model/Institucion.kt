package com.example.sportsyncapp.data.model

import java.util.UUID

data class Institucion(
    val id: UUID,
    val nombre: String,
    val logo: String?,
    val descripcion: String?
)
