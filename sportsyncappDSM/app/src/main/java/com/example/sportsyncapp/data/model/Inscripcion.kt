package com.example.sportsyncapp.data.model

import java.io.Serializable

data class Inscripcion(
    val id: String = "",
    val eventoNombre: String = "",
    val estadoNombre: String = "Pendiente",
    val idEstado: Int = 1,
    val tipo: String = "",
    val equipoNombre: String = "",
    val nombreSolicitante: String = "",
    val fechaInscripcion: String = ""
) : Serializable
