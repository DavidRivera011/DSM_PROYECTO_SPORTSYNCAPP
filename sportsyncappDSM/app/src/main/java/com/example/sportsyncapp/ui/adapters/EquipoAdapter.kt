
package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Equipo

class EquiposAdapter(
    equipos: List<Equipo>,
    private var esAdministrador: Boolean,
    private val onEditar: (Equipo) -> Unit,
    private val onEliminar: (Equipo) -> Unit
) : RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder>() {

    private var equiposOriginales: List<Equipo> = equipos.toList()
    private var equiposFiltrados: List<Equipo> = equipos.toList()
    private var textoBusqueda: String = ""

    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView =
            view.findViewById(R.id.tvTeamName)

        val tvInstitucion: TextView =
            view.findViewById(R.id.tvTeamInstitution)

        val tvDeporte: TextView =
            view.findViewById(R.id.tvTeamSport)

        val layoutAcciones: LinearLayout =
            view.findViewById(R.id.layoutTeamActions)

        val btnEditar: ImageButton =
            view.findViewById(R.id.btnEditTeam)

        val btnEliminar: ImageButton =
            view.findViewById(R.id.btnDeleteTeam)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EquipoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)

        return EquipoViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: EquipoViewHolder,
        position: Int
    ) {
        val equipo = equiposFiltrados[position]

        holder.tvNombre.text = equipo.nombreEquipo
        holder.tvInstitucion.text =
            "Institución: ${equipo.nombreInstitucion ?: "No asignada"}"
        holder.tvDeporte.text =
            "Deporte: ${equipo.nombreDeporte ?: "No asignado"}"

        holder.layoutAcciones.visibility =
            if (esAdministrador) View.VISIBLE else View.GONE

        holder.btnEditar.setOnClickListener {
            if (esAdministrador) onEditar(equipo)
        }

        holder.btnEliminar.setOnClickListener {
            if (esAdministrador) onEliminar(equipo)
        }

        holder.itemView.setOnClickListener(null)
        holder.itemView.isClickable = false
        holder.itemView.isFocusable = false
    }

    override fun getItemCount(): Int = equiposFiltrados.size

    fun actualizarLista(nuevaLista: List<Equipo>) {
        equiposOriginales = nuevaLista.toList()
        filtrarEquipos(textoBusqueda)
    }


    fun filtrarEquipos(texto: String) {
        textoBusqueda = texto.trim()

        android.util.Log.d(
            "BUSQUEDA_EQUIPOS",
            "Texto ingresado: '$textoBusqueda'"
        )

        android.util.Log.d(
            "BUSQUEDA_EQUIPOS",
            "Total de equipos cargados: ${equiposOriginales.size}"
        )

        equiposFiltrados = if (textoBusqueda.isBlank()) {
            equiposOriginales
        } else {
            equiposOriginales.filter { equipo ->
                equipo.nombreEquipo.contains(
                    textoBusqueda,
                    ignoreCase = true
                ) ||
                        equipo.nombreInstitucion.orEmpty().contains(
                            textoBusqueda,
                            ignoreCase = true
                        ) ||
                        equipo.nombreDeporte.orEmpty().contains(
                            textoBusqueda,
                            ignoreCase = true
                        )
            }
        }

        android.util.Log.d(
            "BUSQUEDA_EQUIPOS",
            "Resultados encontrados: ${equiposFiltrados.size}"
        )

        equiposFiltrados.forEach { equipo ->
            android.util.Log.d(
                "BUSQUEDA_EQUIPOS",
                "Resultado: ${equipo.nombreEquipo} | " +
                        "${equipo.nombreInstitucion} | ${equipo.nombreDeporte}"
            )
        }

        notifyDataSetChanged()
    }


    fun actualizarPermisos(esAdmin: Boolean) {
        esAdministrador = esAdmin
        notifyDataSetChanged()
    }
}
