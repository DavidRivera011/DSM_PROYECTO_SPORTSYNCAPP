package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Partido

class PartidosAdapter(
    private var partidos: List<Partido> = emptyList(),
    private var esAdministrador: Boolean = false,
    private val onEditar: (Partido) -> Unit,
    private val onEliminar: (Partido) -> Unit
) : RecyclerView.Adapter<PartidosAdapter.PartidoViewHolder>() {

    class PartidoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvEvento: TextView =
            itemView.findViewById(R.id.tvMatchEvent)

        val tvEquipos: TextView =
            itemView.findViewById(R.id.tvMatchTeams)

        val tvMarcador: TextView =
            itemView.findViewById(R.id.tvMatchScore)

        val tvFecha: TextView =
            itemView.findViewById(R.id.tvMatchDate)

        val tvEstado: TextView =
            itemView.findViewById(R.id.tvMatchStatus)

        val layoutAcciones: View =
            itemView.findViewById(R.id.layoutMatchActions)

        val btnEditar: Button =
            itemView.findViewById(R.id.btnEditMatch)

        val btnEliminar: Button =
            itemView.findViewById(R.id.btnDeleteMatch)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_partido, parent, false)

        return PartidoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PartidoViewHolder,
        position: Int
    ) {
        val partido = partidos[position]

        holder.tvEvento.text =
            partido.eventoNombre

        holder.tvEquipos.text =
            "${partido.equipoLocalNombre} vs ${partido.equipoVisitanteNombre}"

        holder.tvMarcador.text =
            "${partido.marcadorLocal} - ${partido.marcadorVisitante}"

        holder.tvFecha.text = when {
            partido.fecha.isNotBlank() && partido.hora.isNotBlank() ->
                "${partido.fecha} · ${partido.hora}"

            partido.fecha.isNotBlank() ->
                partido.fecha

            partido.hora.isNotBlank() ->
                partido.hora

            else ->
                "Fecha y hora por definir"
        }

        holder.tvEstado.text =
            partido.estadoNombre

        holder.layoutAcciones.visibility =
            if (esAdministrador) View.VISIBLE else View.GONE

        holder.btnEditar.setOnClickListener {
            onEditar(partido)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(partido)
        }
    }

    override fun getItemCount(): Int =
        partidos.size

    fun actualizarLista(nuevaLista: List<Partido>) {
        partidos = nuevaLista
        notifyDataSetChanged()
    }

    fun actualizarPermisos(esAdmin: Boolean) {
        esAdministrador = esAdmin
        notifyDataSetChanged()
    }
}
