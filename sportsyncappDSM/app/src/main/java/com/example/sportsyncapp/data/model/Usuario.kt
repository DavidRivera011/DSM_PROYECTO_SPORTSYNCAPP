package com.example.sportsyncapp.data.model

data class Usuario(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val fechaNacimiento: String = "",
    val rolId: String,
    val fotoPerfil: String? = null
)