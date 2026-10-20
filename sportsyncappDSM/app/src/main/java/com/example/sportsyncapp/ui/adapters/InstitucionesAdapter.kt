package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Institucion

class InstitucionesAdapter(
    private var instituciones: List<Institucion>,
    private var isAdmin: Boolean,
    private val onInstitutionClick: (Institucion) -> Unit,
    private val onDeleteClick: (Institucion) -> Unit
) : RecyclerView.Adapter<InstitucionesAdapter.InstitucionViewHolder>() {

    inner class InstitucionViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val imgLogo: ImageView =
            view.findViewById(R.id.imgLogoInstitucion)

        val tvNombre: TextView =
            view.findViewById(R.id.tvNombreInstitucion)

        val tvDescripcion: TextView =
            view.findViewById(R.id.tvDescripcionInstitucion)

        val btnEliminar: Button =
            view.findViewById(R.id.btnEliminarInstitucion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstitucionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_institucion, parent, false)

        return InstitucionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: InstitucionViewHolder,
        position: Int
    ) {
        val institucion = instituciones[position]

        holder.tvNombre.text = institucion.nombre

        holder.tvDescripcion.text =
            institucion.descripcion?.takeIf { it.isNotBlank() }
                ?: "Sin descripción registrada"

        val urlLogo = institucion.logo?.trim()

        if (!urlLogo.isNullOrBlank()) {
            holder.imgLogo.load(urlLogo) {
                crossfade(true)
                placeholder(R.drawable.ic_sport_ball)
                error(R.drawable.ic_sport_ball)
            }
        } else {
            holder.imgLogo.setImageResource(R.drawable.ic_sport_ball)
        }

        holder.btnEliminar.visibility =
            if (isAdmin) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            onInstitutionClick(institucion)
        }

        holder.btnEliminar.setOnClickListener {
            onDeleteClick(institucion)
        }
    }

    override fun getItemCount(): Int = instituciones.size

    fun updateList(nuevaLista: List<Institucion>) {
        instituciones = nuevaLista
        notifyDataSetChanged()
    }

    fun updatePermissions(esAdmin: Boolean) {
        isAdmin = esAdmin
        notifyDataSetChanged()
    }
}
