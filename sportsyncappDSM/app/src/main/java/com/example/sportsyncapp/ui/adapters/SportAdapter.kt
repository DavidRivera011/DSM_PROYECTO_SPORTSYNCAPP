package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Sport

class SportAdapter(
    private var sports: List<Sport>,
    private val isAdmin: Boolean,
    private val onSportClick: (Sport) -> Unit,
    private val onDeleteClick: (Sport) -> Unit
) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    class SportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvSportName)
        val tvDesc: TextView = view.findViewById(R.id.tvSportDesc)
        val ivIcon: ImageView = view.findViewById(R.id.ivSportIcon)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteSport)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sport, parent, false)
        return SportViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        val sport = sports[position]
        holder.tvName.text = sport.nombreDeporte
        holder.tvDesc.text = sport.descripcion ?: "Disciplina institucional"

        holder.btnDelete.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.btnDelete.setOnClickListener { onDeleteClick(sport) }
        holder.itemView.setOnClickListener { onSportClick(sport) }
    }

    override fun getItemCount(): Int = sports.size

    fun updateList(newList: List<Sport>) {
        sports = newList
        notifyDataSetChanged()
    }
}

