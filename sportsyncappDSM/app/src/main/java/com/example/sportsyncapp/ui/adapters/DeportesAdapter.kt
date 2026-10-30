package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Deporte

class DeportesAdapter(
    private var sports: List<Deporte>,
    private var isAdmin: Boolean,
    private val onSportClick: (Deporte) -> Unit,
    private val onDeleteClick: (Deporte) -> Unit
) : RecyclerView.Adapter<DeportesAdapter.SportViewHolder>() {

    class SportViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvName: TextView =
            view.findViewById(R.id.tvSportName)

        val tvDesc: TextView =
            view.findViewById(R.id.tvSportDesc)

        val btnEdit: ImageButton =
            view.findViewById(R.id.btnEditSport)

        val btnDelete: ImageButton =
            view.findViewById(R.id.btnDeleteSport)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deporte, parent, false)

        return SportViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SportViewHolder,
        position: Int
    ) {
        val sport = sports[position]

        holder.tvName.text = sport.nombreDeporte
        holder.tvDesc.text = "Disciplina deportiva institucional"

        // Ambos botones solo aparecen para administradores.
        val visibilidad =
            if (isAdmin) View.VISIBLE else View.GONE

        holder.btnEdit.visibility = visibilidad
        holder.btnDelete.visibility = visibilidad

        // El lápiz abre el diálogo de edición.
        holder.btnEdit.setOnClickListener {
            if (isAdmin) {
                onSportClick(sport)
            }
        }

        // El bote de basura abre la confirmación de eliminación.
        holder.btnDelete.setOnClickListener {
            if (isAdmin) {
                onDeleteClick(sport)
            }
        }

        // La tarjeta ya no abre el editor al tocarla.
        holder.itemView.setOnClickListener(null)
        holder.itemView.isClickable = false
        holder.itemView.isFocusable = false
    }

    override fun getItemCount(): Int = sports.size

    fun updateList(newList: List<Deporte>) {
        sports = newList
        notifyDataSetChanged()
    }

    fun updatePermissions(esAdmin: Boolean) {
        isAdmin = esAdmin
        notifyDataSetChanged()
    }
}
