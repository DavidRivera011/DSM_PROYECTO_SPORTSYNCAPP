package com.example.sportsyncapp.ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Evento

class EventosAdapter(
    private var eventos: List<Evento>,
    private var esAdministrador: Boolean,
    private val onEditar: (Evento) -> Unit,
    private val onEliminar: (Evento) -> Unit
) : RecyclerView.Adapter<EventosAdapter.EventoViewHolder>() {

    private var eventosFiltrados = eventos.toList()

    class EventoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val nombre: TextView = itemView.findViewById(R.id.tvEventName)
        val fecha: TextView = itemView.findViewById(R.id.tvEventDate)
        val deporte: TextView = itemView.findViewById(R.id.tvEventSport)
        val institucion: TextView =
            itemView.findViewById(R.id.tvEventInstitution)
        val estado: TextView = itemView.findViewById(R.id.tvEventStatus)

        val acciones: View = itemView.findViewById(R.id.layoutEventActions)
        val editar: Button = itemView.findViewById(R.id.btnEditEvent)
        val eliminar: Button = itemView.findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)

        return EventoViewHolder(vista)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: EventoViewHolder,
        position: Int
    ) {
        val evento = eventosFiltrados[position]

        holder.nombre.text = evento.nombre
        val fechaTexto = evento.fecha.toString()
            .let { fecha ->
                try {
                    java.time.LocalDate.parse(fecha)
                        .format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        )
                } catch (_: Exception) {
                    val patron = Regex(
                        """LocalDate\(year=(\d+), month=(\d+), day=(\d+)\)"""
                    )

                    val coincidencia = patron.find(fecha)

                    if (coincidencia != null) {
                        val anio = coincidencia.groupValues[1]
                        val mes = coincidencia.groupValues[2].padStart(2, '0')
                        val dia = coincidencia.groupValues[3].padStart(2, '0')
                        "$dia/$mes/$anio"
                    } else {
                        fecha
                    }
                }
            }

        holder.fecha.text = "Fecha: $fechaTexto"
        holder.deporte.text =
            "Deporte: ${evento.nombreDeporte ?: "Sin deporte"}"
        holder.institucion.text =
            "Institución: ${evento.nombreInstitucion ?: "Sin institución"}"
        holder.estado.text = evento.nombreEstado ?: "Sin estado"

        holder.acciones.visibility =
            if (esAdministrador) View.VISIBLE else View.GONE

        holder.editar.setOnClickListener {
            onEditar(evento)
        }

        holder.eliminar.setOnClickListener {
            onEliminar(evento)
        }
    }

    override fun getItemCount(): Int = eventosFiltrados.size

    fun filtrarEventos(texto: String) {
        val busqueda = texto.trim().lowercase()

        eventosFiltrados = if (busqueda.isEmpty()) {
            eventos.toList()
        } else {
            eventos.filter { evento ->
                evento.nombre.lowercase().contains(busqueda) ||
                        (evento.nombreDeporte ?: "")
                            .lowercase().contains(busqueda) ||
                        (evento.nombreInstitucion ?: "")
                            .lowercase().contains(busqueda) ||
                        (evento.nombreEstado ?: "")
                            .lowercase().contains(busqueda)
            }
        }

        notifyDataSetChanged()
    }

    fun actualizarLista(nuevaLista: List<Evento>) {
        eventos = nuevaLista
        filtrarEventos("")
    }

    fun actualizarPermisos(administrador: Boolean) {
        esAdministrador = administrador
        notifyDataSetChanged()
    }
}
