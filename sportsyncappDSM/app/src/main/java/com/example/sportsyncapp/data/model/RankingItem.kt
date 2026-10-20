package com.example.sportsyncapp.data.model

import java.io.Serializable

data class RankingItem(
    val id: String = "",
    val posicion: Int = 0,
    val nombre: String = "",
    val equipoOInstitucion: String = "",
    val deporte: String = "",
    val puntos: Int = 0
) : Serializable
