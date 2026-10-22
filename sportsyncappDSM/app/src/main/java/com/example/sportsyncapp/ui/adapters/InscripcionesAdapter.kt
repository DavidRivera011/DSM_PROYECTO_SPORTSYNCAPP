package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Inscripcion

class InscripcionesAdapter(
    private var inscripciones: List<Inscripcion>
) : RecyclerView.Adapter<InscripcionesAdapter.InscripcionViewHolder>() {

    class InscripcionViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvEvento: TextView =
            view.findViewById(R.id.tvEventoInscripcion)

        val tvSolicitante: TextView =
            view.findViewById(R.id.tvSolicitanteInscripcion)

        val tvCorreo: TextView =
            view.findViewById(R.id.tvCorreoInscripcion)

        val tvEquipo: TextView =
            view.findViewById(R.id.tvEquipoInscripcion)

        val tvFecha: TextView =
            view.findViewById(R.id.tvFechaInscripcion)

        val tvEstado: TextView =
            view.findViewById(R.id.tvEstadoInscripcion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InscripcionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inscripcion, parent, false)

        return InscripcionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: InscripcionViewHolder,
        position: Int
    ) {
        val inscripcion = inscripciones[position]

        holder.tvEvento.text = inscripcion.eventoNombre

        holder.tvSolicitante.text =
            "Solicitante: ${inscripcion.nombreSolicitante}"

        holder.tvCorreo.text =
            inscripcion.correoSolicitante

        holder.tvEquipo.text =
            if (inscripcion.tipo == "Equipo") {
                "Modalidad: Equipo (${inscripcion.equipoNombre ?: "Equipo"})"
            } else {
                "Modalidad: Individual (${inscripcion.nombreDeportivo ?: "Sin perfil deportivo"})"
            }

        holder.tvFecha.text =
            "Fecha: ${inscripcion.fechaInscripcion}"

        holder.tvEstado.text =
            "Estado: ${inscripcion.estadoNombre}"
    }

    override fun getItemCount(): Int = inscripciones.size

    fun actualizarLista(nuevaLista: List<Inscripcion>) {
        inscripciones = nuevaLista
        notifyDataSetChanged()
    }
}
