package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Match

class MatchAdapter(
    private var matches: List<Match>,
    private val isAdmin: Boolean = false,
    private val onMatchClick: (Match) -> Unit = {},
    private val onUpdateScoreClick: (Match) -> Unit = {}
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTournament: TextView = view.findViewById(R.id.tvMatchTournament)
        val tvDateTime: TextView = view.findViewById(R.id.tvMatchDateTime)
        val tvTeamLocal: TextView = view.findViewById(R.id.tvTeamLocal)
        val tvTeamVisitor: TextView = view.findViewById(R.id.tvTeamVisitor)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
        val tvStatus: TextView = view.findViewById(R.id.tvMatchStatus)
        val btnViewLineup: Button = view.findViewById(R.id.btnViewLineup)
        val btnEditScore: Button = view.findViewById(R.id.btnEditScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        holder.tvTournament.text = match.eventoNombre ?: "Encuentro Institucional"
        holder.tvDateTime.text = "${match.fecha} - ${match.hora}"
        holder.tvTeamLocal.text = match.equipoLocalNombre
        holder.tvTeamVisitor.text = match.equipoVisitanteNombre
        holder.tvScore.text = "${match.marcadorLocal} - ${match.marcadorVisitante}"
        holder.tvStatus.text = "Estado: ${match.estadoNombre}"

        holder.itemView.setOnClickListener { onMatchClick(match) }
        holder.btnViewLineup.setOnClickListener { onMatchClick(match) }

        if (isAdmin) {
            holder.btnEditScore.visibility = View.VISIBLE
            holder.btnEditScore.setOnClickListener { onUpdateScoreClick(match) }
        } else {
            holder.btnEditScore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = matches.size

    fun updateList(newList: List<Match>) {
        matches = newList
        notifyDataSetChanged()
    }
}

