package com.example.sportsyncapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeporteEvent(
    @SerializedName("id_evento")
    val idEvento: Int = 0,

    @SerializedName("id_deporte")
    val idDeporte: Int? = null,

    @SerializedName("deporte_nombre")
    val deporteNombre: String = "",

    @SerializedName("id_institucion")
    val idInstitucion: Int? = null,

    @SerializedName("institucion_nombre")
    val institucionNombre: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("id_estado")
    val idEstado: Int = 1,

    @SerializedName("estado_nombre")
    val estadoNombre: String = "Pendiente",

    @SerializedName("descripcion")
    val descripcion: String? = null
) : Serializable
