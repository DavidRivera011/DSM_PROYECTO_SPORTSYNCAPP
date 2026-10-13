package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.MatchParticipant

class ParticipantAdapter(
    private var participants: List<MatchParticipant>
) : RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {

    class ParticipantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvPlayerName)
        val tvPosition: TextView = view.findViewById(R.id.tvPlayerPosition)
        val tvTitular: TextView = view.findViewById(R.id.tvPlayerTitular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant = participants[position]
        holder.tvName.text = participant.nombreJugador
        holder.tvPosition.text = participant.posicion ?: "Jugador"

        if (participant.titular) {
            holder.tvTitular.text = "Titular"
            holder.tvTitular.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_approved)
        } else {
            holder.tvTitular.text = "Suplente"
            holder.tvTitular.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_pending)
        }
    }

    override fun getItemCount(): Int = participants.size

    fun updateList(newList: List<MatchParticipant>) {
        participants = newList
        notifyDataSetChanged()
    }
}
