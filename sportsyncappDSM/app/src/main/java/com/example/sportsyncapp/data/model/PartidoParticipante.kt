package com.example.sportsyncapp.data.model

import java.io.Serializable

data class PartidoParticipante(
    val id: String = "",
    val nombreJugador: String = "",
    val posicion: String = "",
    val titular: Boolean = false
) : Serializable
