package com.example.sportsyncapp.data.model

data class HomeResumen(
    val nombreUsuario: String,
    val totalEventos: Int = 0,
    val totalPartidos: Int = 0,
    val totalInscripciones: Int = 0,
    val totalDeportes: Int = 0,
    val esAdministrador: Boolean = false
)