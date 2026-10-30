package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Solicitud
import com.google.android.material.button.MaterialButton
import java.util.UUID

class SolicitudesAdminAdapter(
    private var solicitudes: List<Solicitud>,
    private val alAprobar: (UUID) -> Unit,
    private val alRechazar: (UUID) -> Unit
) : RecyclerView.Adapter<SolicitudesAdminAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNombre: TextView =
            view.findViewById(R.id.tvNombreSolicitudAdmin)

        val tvUsuario: TextView =
            view.findViewById(R.id.tvUsuarioSolicitudAdmin)

        val tvCorreo: TextView =
            view.findViewById(R.id.tvCorreoSolicitudAdmin)

        val tvDeporte: TextView =
            view.findViewById(R.id.tvDeporteSolicitudAdmin)

        val tvInstitucion: TextView =
            view.findViewById(R.id.tvInstitucionSolicitudAdmin)

        val tvFecha: TextView =
            view.findViewById(R.id.tvFechaSolicitudAdmin)

        val tvEstado: TextView =
            view.findViewById(R.id.tvEstadoSolicitudAdmin)

        val btnAprobar: MaterialButton =
            view.findViewById(R.id.btnAprobarSolicitud)

        val btnRechazar: MaterialButton =
            view.findViewById(R.id.btnRechazarSolicitud)

        val layoutAcciones: View =
            view.findViewById(R.id.layoutAccionesSolicitud)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_solicitud_admin,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val solicitud = solicitudes[position]

        holder.tvNombre.text =
            solicitud.nombreDeportivo?.takeIf { it.isNotBlank() }
                ?: solicitud.nombrePerfil

        holder.tvUsuario.text =
            "Solicitante: ${solicitud.nombreUsuario} ${solicitud.apellidoUsuario}"

        holder.tvCorreo.text = solicitud.correoUsuario

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

        val pendiente = solicitud.estadoNombre.equals(
            "Pendiente",
            ignoreCase = true
        )

        holder.layoutAcciones.visibility =
            if (pendiente) View.VISIBLE else View.GONE

        holder.btnAprobar.setOnClickListener {
            alAprobar(solicitud.id)
        }

        holder.btnRechazar.setOnClickListener {
            alRechazar(solicitud.id)
        }
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
