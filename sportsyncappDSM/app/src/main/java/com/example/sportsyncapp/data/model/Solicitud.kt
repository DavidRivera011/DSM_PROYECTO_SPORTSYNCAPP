package com.example.sportsyncapp.data.model

import java.util.UUID

data class Solicitud(
    val id: UUID,
    val usuarioId: String,
    val nombreUsuario: String,
    val apellidoUsuario: String,
    val correoUsuario: String,
    val deporteId: UUID,
    val nombreDeporte: String,
    val tipoPerfilId: UUID,
    val nombrePerfil: String,
    val institucionId: UUID?,
    val nombreInstitucion: String?,
    val equipoId: UUID?,
    val nombreEquipo: String?,
    val nombreDeportivo: String?,
    val fotoDeportiva: String?,
    val fechaSolicitud: String,
    val estadoId: UUID,
    val estadoNombre: String,
    val revisorNombre: String
)
