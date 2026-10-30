package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Solicitud

class SolicitudesAdapter(
    private var solicitudes: List<Solicitud>
) : RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder>() {

    class SolicitudViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvNombre: TextView =
            view.findViewById(R.id.tvNombreSolicitud)

        val tvUsuario: TextView =
            view.findViewById(R.id.tvUsuarioSolicitud)

        val tvCorreo: TextView =
            view.findViewById(R.id.tvCorreoSolicitud)

        val tvDeporte: TextView =
            view.findViewById(R.id.tvDeporteSolicitud)

        val tvInstitucion: TextView =
            view.findViewById(R.id.tvInstitucionSolicitud)

        val tvFecha: TextView =
            view.findViewById(R.id.tvFechaSolicitud)

        val tvEstado: TextView =
            view.findViewById(R.id.tvEstadoSolicitud)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SolicitudViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitud, parent, false)

        return SolicitudViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SolicitudViewHolder,
        position: Int
    ) {
        val solicitud = solicitudes[position]

        holder.tvNombre.text =
            solicitud.nombreDeportivo?.takeIf { it.isNotBlank() }
                ?: solicitud.nombrePerfil

        // Los datos personales solo se muestran si la consulta los devuelve.
        val nombreCompleto = listOf(
            solicitud.nombreUsuario,
            solicitud.apellidoUsuario
        ).filter { it.isNotBlank() }.joinToString(" ")

        if (nombreCompleto.isNotBlank()) {
            holder.tvUsuario.visibility = View.VISIBLE
            holder.tvUsuario.text = "Solicitante: $nombreCompleto"
        } else {
            holder.tvUsuario.visibility = View.GONE
        }

        if (solicitud.correoUsuario.isNotBlank()) {
            holder.tvCorreo.visibility = View.VISIBLE
            holder.tvCorreo.text = solicitud.correoUsuario
        } else {
            holder.tvCorreo.visibility = View.GONE
        }

        holder.tvDeporte.text =
            "Deporte: ${solicitud.nombreDeporte} · Perfil: ${solicitud.nombrePerfil}"

        holder.tvInstitucion.text = buildString {
            append("Institución: ")
            append(solicitud.nombreInstitucion ?: "No especificada")
            append("\nEquipo: ")
            append(solicitud.nombreEquipo ?: "No especificado")
        }

        holder.tvFecha.text =
            "Fecha de solicitud: ${formatearFecha(solicitud.fechaSolicitud)}"

        holder.tvEstado.text =
            "Estado: ${solicitud.estadoNombre}"
    }

    private fun formatearFecha(fecha: Any?): String {
        if (fecha == null) return "Sin fecha"

        val patron = Regex(
            """LocalDate\(year=(\d+), month=(\d+), day=(\d+)\)"""
        )

        val resultado = patron.find(fecha.toString())

        return if (resultado != null) {
            val anio = resultado.groupValues[1]
            val mes = resultado.groupValues[2].padStart(2, '0')
            val dia = resultado.groupValues[3].padStart(2, '0')

            "$dia/$mes/$anio"
        } else {
            fecha.toString()
        }
    }

    override fun getItemCount(): Int = solicitudes.size

    fun actualizarLista(nuevaLista: List<Solicitud>) {
        solicitudes = nuevaLista
        notifyDataSetChanged()
    }
}
